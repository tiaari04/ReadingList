# **Overview**

The Reading List App is a Java-based application that helps users manage their personal book collections. It allows users to search for books using the Google Books API and add selected books to a local library. Users can track their reading status, view detailed book information, and perform actions such as deleting books from the library or updating their read status.

# **Features**

**Book Search:** Search for books by title, author, or ISBN using the Google Books API.

**Book Details:** View detailed information about a book, including:

* Title

* Author(s)

* Description

* Publication Year

* Genre

* Thumbnail

* Page Count

**Library Management:**

Add books to your library.

View books in your library, including details like title, authors, and ISBN.

Track and update the reading status of books (Read, Unread, Reading).

Delete books from your library. <br/>

**Backend Integration:**

Uses Volley and Node.js to handle HTTP requests.

Stores book data in a local PostgreSQL database.

# **Technologies Used**

Frontend:

* Java (Android)

* Volley for HTTP requests

Backend:

* Node.js for server-side logic

* PostgreSQL for database storage

API:

* Google Books API for book search and metadata

# **Installation and Setup**
1. Clone the Repository
```
git clone <repository-url>
cd reading-list-app
```
2. Setup backend <br/>
Backend Configuration:
The backend uses a PostgreSQL database. Detailed setup instructions are available upon request.

# **Usage**
1. Launch the app and search for books by title, author, or ISBN.
2. Click on a book to view its details.
3. Add desired books to your library.
4. Manage your library by viewing, updating read status, or deleting books.

# **Future Improvements**
* Implement search bar to search through all the books in a user's library
* Allow users to have a friends list and view other users' libraries
