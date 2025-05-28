class User {
  constructor(email, fullName, role, authority, bio, reviews, rating, price, specialties, createdAt) {
    this.email = email
    this.fullName = fullName
    this.role = role
    this.authority = authority
    this.bio = bio
    this.reviews = reviews
    this.rating = rating
    this.price = price
    this.specialties = specialties
    this.createdAt = createdAt
  }

  static fromMap(map) {
    return new User(
        map['email'],
        map['fullName'],
        map['role'],
        map['authority'],
        map['bio'],
        map['reviews'],
        map['rating'],
        map['price'],
        map['specialties'],
        map['createdAt']
    )
  }
}

class Review {
  constructor(id, client, specialist, rating, comment, createdAt) {
    this.id = id
    this.client = client
    this.specialist = specialist
    this.rating = rating
    this.comment = comment
    this.createdAt = createdAt
  }
}

export { User, Review }
