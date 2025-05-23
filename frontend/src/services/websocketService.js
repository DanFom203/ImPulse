import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { getTokenFromStorage } from '@/services/localData.js';

let stompClient = null;
let connectionPromise = null;

export function connectWebSocket(onMessageCallback) {
    // Возвращаем существующее подключение, если оно в процессе
    if (connectionPromise) return connectionPromise;

    connectionPromise = new Promise((resolve, reject) => {
        try {
            const token = getTokenFromStorage();
            if (!token) throw new Error('No auth token');

            const socket = new SockJS('http://localhost:8080/ws');

            stompClient = new Client({
                webSocketFactory: () => socket,
                connectHeaders: { Authorization: `Bearer ${token}` },
                onConnect: () => {
                    console.log('✅ WebSocket connected');
                    stompClient.subscribe('/user/queue/reply', message => {
                        onMessageCallback(JSON.parse(message.body));
                    });
                    resolve(stompClient);
                },
                onStompError: frame => {
                    reject(new Error(`STOMP error: ${frame.headers.message}`));
                },
                onWebSocketClose: (event) => {
                    console.warn(`Connection closed: ${event.code} - ${event.reason}`);

                    // Автоматический реконнект только для определенных кодов
                    if ([1006, 1001].includes(event.code)) {
                        setTimeout(() => {
                            console.log("Reconnecting...");
                            connectWebSocket(onMessageCallback);
                        }, 5000);
                    }

                    // Обработка истекшего токена
                    if (event.code === 4001) {
                        localStorage.removeItem('token');
                        window.location.reload();
                    }
                }
            });

            stompClient.activate();
        } catch (error) {
            reject(error);
        }
    });

    return connectionPromise;
}

export async function sendMessage(payload) {
    if (!stompClient?.active) {
        await connectWebSocket(); // Переподключаемся при необходимости
    }

    stompClient.publish({
        destination: '/app/chat',
        body: JSON.stringify(payload)
    });
}