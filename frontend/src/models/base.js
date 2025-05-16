class User {
  constructor(email, fullName, role, bio, rating, price, specialties) {
    this.email = email
    this.fullName = fullName
    this.role = role
    this.bio = bio
    this.rating = rating
    this.price = price
    this.specialties = specialties
  }

  static fromMap(map) {
    return new User(
      map['email'],
      map['fullName'],
      map['role'],
      map['bio'],
      map['rating'],
      map['price'],
      map['specialties']
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
