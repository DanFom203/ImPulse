class RequestData {
  constructor(loading, error) {
    this.loading = loading
    this.error = error
  }

  startLoading() {
    this.loading = true
    this.clearError()
  }

  stopLoading() {
    this.loading = false
  }

  setError(error) {
    this.error = error
  }

  clearError() {
    this.error = null
  }
}

export { RequestData }
