import { describe, it, expect, beforeEach, vi } from 'vitest'
import { setActivePinia, createPinia } from 'pinia'
import { useReviewStore } from '@/stores/reviewStore'
import * as api from '@/services/api'

describe('reviewStore', () => {
    beforeEach(() => {
        setActivePinia(createPinia())
    })

    it('fetches reviews successfully', async () => {
        const mockReviews = [{ id: 1, comment: 'Test review' }]
        vi.spyOn(api, 'apiFetchClientReviews').mockResolvedValue(mockReviews)

        const store = useReviewStore()
        await store.fetchReviews()

        expect(store.reviews).toEqual(mockReviews)
        expect(store.requestData.loading).toBe(false)
        expect(store.requestData.error).toBe(null)
    })

    it('handles error during fetchReviews', async () => {
        const error = new Error('Failed to fetch reviews')
        vi.spyOn(api, 'apiFetchClientReviews').mockRejectedValue(error)

        const store = useReviewStore()
        await store.fetchReviews()

        expect(store.reviews).toEqual([])
        expect(store.requestData.loading).toBe(false)
        expect(store.requestData.error).toBe(error)
    })

    it('creates a review successfully', async () => {
        vi.spyOn(api, 'apiCreateReview').mockResolvedValue({})

        const store = useReviewStore()
        await store.createReview(1, { comment: 'Great!' })

        expect(store.requestData.loading).toBe(false)
        expect(store.requestData.error).toBe(null)
    })

    it('handles error during createReview', async () => {
        const error = new Error('Create failed')
        vi.spyOn(api, 'apiCreateReview').mockRejectedValue(error)

        const store = useReviewStore()
        await store.createReview(1, { comment: 'Oops' })

        expect(store.requestData.loading).toBe(false)
        expect(store.requestData.error).toBe(error)
    })
})
