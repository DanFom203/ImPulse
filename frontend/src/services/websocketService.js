
    import { Stomp } from '@stomp/stompjs'
    import { getTokenFromStorage } from '@/services/localData.js'
    import axios from 'axios'

    export default {
    props: {
    interlocutor: {
    type: Object,
    required: true
}
},
    data() {
    return {
    stompClient: null,
    connected: false,
    messages: [],
    messageText: '',
    currentUserId: null,
    pollingInterval: null
}
},
    mounted() {
    console.log('🔍 mounted: компонент загружен')
    const token = getTokenFromStorage()
    if (token) {
    const payload = JSON.parse(atob(token.split('.')[1]))
    this.currentUserId = payload.id
    console.log('🔍 currentUserId установлен:', this.currentUserId)
} else {
    console.warn('⚠️ mounted: нет токена')
}

    this.loadChatHistory()
    this.connect()

    this.pollingInterval = setInterval(this.pollForNewMessages, 10000)
},
    beforeDestroy() {
    //  очищаем таймер при уничтожении компонента
    if (this.pollingInterval) {
    clearInterval(this.pollingInterval)
    this.pollingInterval = null
}
},
    methods: {
    async pollForNewMessages() {
    console.log('🔄 pollForNewMessages: опрос сервера...')
    const token = getTokenFromStorage()
    if (!token) {
    console.warn('⚠️ pollForNewMessages: нет токена')
    return
}

    try {
    const response = await axios.get('http://localhost:8080/api/chat/history', {
    headers: { Authorization: `Bearer ${token}` },
    params: { interlocutorId: this.interlocutor.id }
})

    const newMessages = response.data

    if (newMessages.length > this.messages.length) {
    console.log('📥 pollForNewMessages: найдены новые сообщения')
    this.messages = newMessages
    this.scrollToBottom()
} else {
    console.log('📭 pollForNewMessages: новых сообщений нет')
}
} catch (err) {
    console.error('❌ pollForNewMessages: ошибка при запросе:', err)
}
},
    async loadChatHistory() {
    console.log('🔍 loadChatHistory: Загрузка истории сообщений для interlocutorId=', this.interlocutor.id)
    const token = getTokenFromStorage()
    if (!token) {
    console.warn('⚠️ loadChatHistory: нет токена, выход')
    return alert('Not authenticated')
}

    try {
    const response = await axios.get('http://localhost:8080/api/chat/history', {
    headers: { Authorization: `Bearer ${token}` },
    params: { interlocutorId: this.interlocutor.id }
})
    console.log('🔍 loadChatHistory: получена история сообщений', response.data)
    this.messages = response.data
    this.scrollToBottom()
} catch (error) {
    console.error('❌ loadChatHistory: ошибка загрузки истории:', error.response?.data || error)
}
},
    connect() {
    console.log('🔍 connect: начинаем подключение к WebSocket')
    const token = getTokenFromStorage()
    if (!token) {
    console.warn('⚠️ connect: нет токена, выход')
    return alert('Not authenticated')
}

    if (this.connected) {
    console.log('ℹ️ connect: уже подключен, выход')
    return
}

    this.stompClient = Stomp.client('ws://localhost:8080/ws')
    this.stompClient.reconnect_delay = 5000

    this.stompClient.connect(
{ Authorization: `Bearer ${token}` },
    frame => {
    this.connected = true
    console.log('✅ WebSocket подключён:', frame)
    this.stompClient.subscribe('user/queue/reply', this.onMessageReceived)
    console.log('🔍 WebSocket подписка на /queue/reply установлена')
},
    error => {
    console.error('❌ STOMP connection error:', error)
}
    )
},
    onMessageReceived(msg) {
    console.log('🔍 onMessageReceived: пришло сообщение:', msg);

    const token = getTokenFromStorage();
    if (!token) {
    console.warn('⚠️ onMessageReceived: нет токена');
    return;
}

    const payload = JSON.parse(msg.body);
    console.log('🔍 onMessageReceived: разобранный payload:', payload);
    console.log('🧾 onMessageReceived: JSON содержимое msg.body:\n', JSON.stringify(payload, null, 2));

    // Проверяем, что сообщение относится к текущему собеседнику
    if (
    (payload.senderId === this.interlocutor.id && payload.receiverId === this.currentUserId) ||
    (payload.senderId === this.currentUserId && payload.receiverId === this.interlocutor.id)
    ) {
    console.log('🔍 onMessageReceived: сообщение относится к текущему собеседнику, добавляем в массив');
    this.messages.push(payload);
    this.scrollToBottom();
} else {
    console.log('ℹ️ onMessageReceived: сообщение не для текущего собеседника, игнорируем');
}
},
    sendMessage() {
    console.log('🔍 sendMessage: попытка отправки сообщения')
    if (!this.connected) {
    console.warn('⚠️ sendMessage: WebSocket не подключён')
    return
}
    if (!this.messageText.trim()) {
    console.warn('⚠️ sendMessage: пустое сообщение')
    return
}

    const token = getTokenFromStorage()
    if (!token) {
    console.warn('⚠️ sendMessage: нет токена')
    return alert('Not authenticated')
}

    const payload = {
    receiverId: this.interlocutor.id,
    body: this.messageText.trim()
}

    console.log('🔍 sendMessage: отправляем через STOMP:', payload)
    this.stompClient.send('/app/chat',
{ Authorization: `Bearer ${token}` },
    JSON.stringify(payload)
    )

    this.messageText = ''
    this.messages.push(payload)
    this.scrollToBottom()
    console.log('🔍 sendMessage: поле ввода очищено')
},
    scrollToBottom() {
    this.$nextTick(() => {
    const container = this.$refs.chatContainer
    if (container) {
    container.scrollTop = container.scrollHeight
} else {
    console.warn('⚠️ scrollToBottom: контейнер не найден')
}
})
}
},
}
