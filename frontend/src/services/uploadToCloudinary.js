import axios from 'axios'

export async function uploadToCloudinary(file) {
    const CLOUD_NAME = 'dns3r68pe'
    const UPLOAD_PRESET = 'my_unsigned'
    const url = `https://api.cloudinary.com/v1_1/${CLOUD_NAME}/upload`

    const formData = new FormData()
    formData.append('file', file)
    formData.append('upload_preset', UPLOAD_PRESET)

    try {
        const response = await axios.post(url, formData)
        return response.data.secure_url
    } catch (error) {
        console.error('Ошибка Cloudinary:', error.response || error)
        throw error
    }
}