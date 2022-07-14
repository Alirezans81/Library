import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Scanner;

class App {
	
	static class Storage {
		final static String booksFilePath = "books.txt";
		final static String booksCategoriesFilePath = "booksCategories.txt";
		final static String membersFilePath = "members.txt";
		final static String borrowingTimeFilePath = "borrowingTime.txt";
		
		static App.Manager manager = new App.Manager("admin", "admin", "admin");
		static ArrayList<App.Book> books = new ArrayList<App.Book>();
		static ArrayList<App.Category> booksCategories = new ArrayList<Category>();
		static ArrayList<App.Member> members = new ArrayList<App.Member>();
		static HashMap<App.Book, Long> borrowingTime = new HashMap<Book, Long>();
		
		static public void save() throws IOException {
//			books
			FileWriter writer = new FileWriter(booksFilePath); 
			for(Book book: books) {
				String line = "";
				line += book.id + " " + book.name + " " + book.author + " " + book.publisher + " " + book.yearOfPublication + " " + book.category.name + " " + book.lended + " " + book.borrowerId;
				writer.write(line + System.lineSeparator());
			}
			writer.close();
//			end of books
			
//			booksCategories
			writer = new FileWriter(booksCategoriesFilePath); 
			for(Category category: booksCategories) {
				String line = "";
				line += category.id + " " + category.name;
				writer.write(line + System.lineSeparator());
			}
			writer.close();
//			end of booksCategories
			
//			members
			writer = new FileWriter(membersFilePath); 
			for(Member member: members) {
				String line = "";
				line += member.name + " " + member.username + " " + member.password;
				writer.write(line + System.lineSeparator());
			}
			writer.close();
//			end of members
			
//			borrowingTime
			writer = new FileWriter(borrowingTimeFilePath); 
			for(Entry<App.Book, Long> entry: borrowingTime.entrySet()) {
				String line = "";
				line += entry.getKey().id + " " + entry.getValue();
				writer.write(line + System.lineSeparator());
			}
			writer.close();
//			end of borrowingTime
		}
		
		static public void load() throws FileNotFoundException {
//			books
			File myFile = new File(booksFilePath);
			Scanner myReader = new Scanner(myFile);
			while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        String[] vars = data.split(" ");
		        Manager m = new Manager();
		        m.addBook(Integer.parseInt(vars[0]), vars[1], vars[2], vars[3], Integer.parseInt(vars[4]), vars[5]);
		        util u = new util();
		        u.getBookById(Integer.parseInt(vars[0])).lended = Boolean.parseBoolean(vars[6]);
		        u.getBookById(Integer.parseInt(vars[0])).borrowerId = vars[7];
		    }
		    myReader.close();
//			end of books
		    
//		    booksCategories
		    myFile = new File(booksCategoriesFilePath);
			myReader = new Scanner(myFile);
			while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        String[] vars = data.split(" ");
		        Category theCategory = new Category(Integer.parseInt(vars[0]), vars[1]);
		        theCategory.loadBooksInTheCategory();
		        booksCategories.add(theCategory);
		    }
		    myReader.close();
//		    end of booksCategories
		    
//		    member
		    myFile = new File(membersFilePath);
			myReader = new Scanner(myFile);
			while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        String[] vars = data.split(" ");
		        App.Manager m = new Manager();
		        m.addMember(vars[0], vars[1], vars[2]);
		    }
			myReader.close();
//		    end of member
		    
//		    borrowingTime
		    myFile = new File(borrowingTimeFilePath);
			myReader = new Scanner(myFile);
			while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        String[] vars = data.split(" ");
		        util u = new util();
		        Book theBook = u.getBookById(Integer.parseInt(vars[0]));
		        borrowingTime.put(theBook, Long.parseLong(vars[1]));
		    }
			myReader.close();
//		    end of borrowingTime
		}
	}
	
	static class util {
		public boolean failed;
		
		Book getBookById(int id) {
			failed = false;
			boolean exist = false;
			Book theBook = null;
			for (Book book: Storage.books) {
				if (book.id == id) {
					theBook = book;
					exist = true;
				}
			}
			if (exist == false) {
				System.out.println("The book not found!");
				failed = true;
			}
			
			return theBook;
		}
		
		Book getBookByName(String name) {
			failed = false;
			boolean exist = false;
			Book theBook = null;
			for (Book book: Storage.books) {
				if (book.name == name) {
					theBook = book;
					exist = true;
				}
			}
			if (exist == false) {
				System.out.println("The book not found!");
				failed = true;
			}
			
			return theBook;
		}
	
		Category getCategoryById(int id) {
			failed = false;
			boolean exist = false;
			Category theCategory = null;
			for (Category category: Storage.booksCategories) {
				if (theCategory.id == id) {
					theCategory = category;
					exist = true;
				}
			}
			if (exist == false) {
				System.out.println("The category not found!");
				failed = true;
			}
			
			return theCategory;
		}

		Category getCategoryByName(String name) {
			failed = false;
			boolean exist = false;
			Category theCategory = null;
			for (Category category: Storage.booksCategories) {
				if (theCategory.name == name) {
					theCategory = category;
					exist = true;
				}
			}
			if (exist == false) {
				System.out.println("The category not found!");
				failed = true;
			}
			
			return theCategory;
		}

		String getPassedTime(long theTime) {
			long passedTime = System.currentTimeMillis() - theTime;
			long monthToMillis = (long) 2629743833.3;
			long dayToMillis = 86400000;
			long hourToMillis = 3600000;
			long minuteToMillis = 60000;			
			long secondToMillis = 1000;			
			int month = (int) (passedTime / monthToMillis);
			int day = (int) ((passedTime - (month * monthToMillis)) / dayToMillis);
			int hour = (int) ((passedTime - (month * monthToMillis + day * dayToMillis)) / hourToMillis);
			int minute = (int) ((passedTime - (month * monthToMillis + day * dayToMillis + hour * hourToMillis)) / minuteToMillis);
			int second = (int) ((passedTime - (month * monthToMillis + day * dayToMillis + hour * hourToMillis + minute * minuteToMillis)) / secondToMillis);
			
			String result = month + "months " + day + "days " + hour + "hours " + minute + "minutes " + second + "seconds";
			return result;
		}
		
		boolean usernameCheck(String username, String For) {
			for (App.Member member: App.Storage.members) {
				if (member.username.equals(username)) {
					if (For.equals("signUp")) {
						System.out.println("The Username Already Taken!");
					}
					return true;
				}
			}
			if (For.equals("signIn")) {
				System.out.println("The Username Not Found!");
			}
			return false;
		}
	}
	
	static class Book {
		
		private int id;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		
		private String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		private String author;
		public String getAuthor() {
			return author;
		}
		public void setAuthor(String author) {
			this.author = author;
		}
		
		private String publisher;
		public String getPublisher() {
			return publisher;
		}
		public void setPublisher(String publisher) {
			this.publisher = publisher;
		}
		
		private int yearOfPublication;
		public int getYearOfPublication() {
			return yearOfPublication;
		}
		public void setYearOfPublication(int yearOfPublication) {
			this.yearOfPublication = yearOfPublication;
		}
		
		private Category category;
		public Category getCategory() {
			return category;
		}
		public void setCategory(Category category) {
			this.category = category;
		}
		
		private boolean lended;
		public boolean getLended() {
			return lended;
		}
		public void setLended(boolean lended) {
			this.lended = lended;
		}
		private String borrowerId = "null";
		public String getBorrowerId() {
			return borrowerId;
		}
		public void setBorrowerId(String borrowerId) {
			this.borrowerId = borrowerId;
		}
		
		void isLended() {
			util u = new util();
			if (this.lended == false) System.out.println("The book is not lended.");
			else System.out.println("The book is lended by id: " + this.borrowerId + " for " + u.getPassedTime(Storage.borrowingTime.get(this)));
		}
			
		public Book() {
			Category Lended = new Category();
			Lended.id = -1;
			Lended.name = "Lended";
			Storage.booksCategories.add(Lended);
			
			Category donation = new Category();
			donation.id = -2;
			donation.name = "Donation";
			Storage.booksCategories.add(donation);
		}
	}
	
	
	static class Category {
		public Category() {
			
		}
		
		public Category(int id, String name) {
			this.id = id;
			this.name = name;
		}
		
		private int id;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		
		private String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		private int quantity = 0;	
		public int getQuantity() {
			return quantity;
		}
		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
		
		ArrayList<App.Book> booksInTheCategory = new ArrayList<App.Book>();
		
		public void loadBooksInTheCategory() {
			for (Book book: Storage.books) {
				if (name.equals(book.category.name)) {
					booksInTheCategory.add(book);
					quantity++;
				}
			}
		}
	}
	
	
	static class Manager {
		public Manager() {
			
		}
		
		public Manager(String name, String username, String password) {
			this.name = name;
			this.username = username;
			this.password = password;
		}
		
		private String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		private String username;
		public String getUsername() {
			return username;
		}		
		public void setUsername(String username) {
			this.username = username;
		}
		
		private String password;
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		
		void getBooks() {
			for (Book book: Storage.books) {
				System.out.print("id: " + book.id + " ");
				System.out.print("name: " + book.name + " ");
				System.out.print("author: " + book.author + " ");
				System.out.print("publisher: " + book.publisher + " ");
				System.out.print("yearOfPublication: " + book.yearOfPublication + " ");
				System.out.print("category: " + book.category.name + " ");
				System.out.print("is lended: " + book.lended + " ");
				System.out.println();			
			}
		}
		
		void getBookById(int id) {
			util u = new util();
			Book theBook = u.getBookById(id);
			
			System.out.print("id: " + theBook.id + " ");
			System.out.print("name: " + theBook.name + " ");
			System.out.print("author: " + theBook.author + " ");
			System.out.print("publisher: " + theBook.publisher + " ");
			System.out.print("yearOfPublication: " + theBook.yearOfPublication + " ");
			System.out.print("category: " + theBook.category.name + " ");
			System.out.print("is lended: " + theBook.lended + " ");
			System.out.println();
		}
		
		void getBookByName(String name) {
			util u = new util();
			Book theBook = u.getBookByName(name);
			
			System.out.print("id: " + theBook.id + " ");
			System.out.print("name: " + theBook.name + " ");
			System.out.print("author: " + theBook.author + " ");
			System.out.print("publisher: " + theBook.publisher + " ");
			System.out.print("yearOfPublication: " + theBook.yearOfPublication + " ");
			System.out.print("category: " + theBook.category.name + " ");
			System.out.print("is lended: " + theBook.lended + " ");
			System.out.println();
		}
		
		void addBook(int id, String name, String author, String publisher, int yearOfPublication, String category) {
			boolean alreadyExisted = false;
			for (Book book: Storage.books) {
				if (book.id == id) {
					alreadyExisted = true;
					break;
				}
			}
			
			if (alreadyExisted == false) {
				Book theBook = new Book();
				theBook.id = id;
				theBook.name = name;
				theBook.author = author;
				theBook.publisher = publisher;
				theBook.yearOfPublication = yearOfPublication;
				
				boolean categoryAlreadyExisted = false;
				for (Category categoryInBooksCategories : Storage.booksCategories) {
					if (categoryInBooksCategories.name == category) {
						theBook.category = categoryInBooksCategories;
						Storage.books.add(theBook);
						categoryInBooksCategories.quantity++;
						categoryInBooksCategories.booksInTheCategory.add(theBook);
						System.out.println("The book added!");
						categoryAlreadyExisted = true;
						break;
					}
				}
				
				if (categoryAlreadyExisted == false) {
					Category categoryCreated = new Category();
					
					Random rn = new Random();
					int selectedId = rn.nextInt();
					boolean idExisted = false;
					for (Category categoryInBooksCategories : Storage.booksCategories) {
						if (categoryInBooksCategories.id == selectedId) {
							idExisted = true;
							break;
						}
					}
					while (idExisted == true) {
						selectedId = rn.nextInt();
						boolean idExistedInTheRow = false;
						for (Category categoryInBooksCategories : Storage.booksCategories) {
							if (categoryInBooksCategories.id == selectedId) {
								idExistedInTheRow = true;
								break;
							}
						}
						
						if (idExistedInTheRow == false) idExisted = false;
					}
					
					categoryCreated.id = selectedId;
					categoryCreated.name = category;
					Storage.booksCategories.add(categoryCreated);
					
					theBook.category = categoryCreated;
					Storage.books.add(theBook);
					categoryCreated.quantity++;
					categoryCreated.booksInTheCategory.add(theBook);
					System.out.println("The book added by random category id!");
				}
			} else System.out.println("The book already existed!");
		}
		
		void removeBookById(int id) {
			boolean exist = false;
			for (Book book: Storage.books) {
				if (book.id == id) {
					Storage.borrowingTime.remove(book);
					book.category.booksInTheCategory.remove(book);
					book.category.quantity--;
					Storage.books.remove(book);
					exist = true;
				}
			}
			
			if (exist == true) System.out.println("The book removed!"); 
			else System.out.println("The book not found!");
		}
		
		void removeBookByName(String name) {
			boolean exist = false;
			for (Book book: Storage.books) {
				if (book.name == name) {
					Storage.borrowingTime.remove(book);
					book.category.booksInTheCategory.remove(book);
					book.category.quantity--;
					Storage.books.remove(book);
					exist = true;
				}
			}
			
			if (exist == true) System.out.println("The book removed!"); 
			else System.out.println("The book not found!");
		}
		
		void getBooksCategories() {
			for (Category category: Storage.booksCategories) {
				System.out.print("id: " + category.id + " ");
				System.out.print("name: " + category.name + " ");
				System.out.print("quantity: " + category.quantity + " ");
				System.out.println();
			}
		}
		
		void addBookByIdToCategoryByName(int bookId, String categoryName) {
			util u = new util();
			boolean failed = false;
			
			Book theBook = u.getBookById(bookId);
			if (u.failed == true) {
				failed = true;
			}
			
			Category theCategory = u.getCategoryByName(categoryName);
			if (u.failed == true) {
				failed = true;
			}
			
			if (failed == false) {
				theBook.category = theCategory;
				theCategory.booksInTheCategory.add(theBook);
				theCategory.quantity++;
				
				System.out.println("The book id: " + bookId + "added to category name: " + categoryName + "!"); 
			} else System.out.println("Failed because of the above reason!");
		}
		
		void addBookByNameToCategoryByName(String bookName, String categoryName) {
			util u = new util();
			boolean failed = false;
			
			Book theBook = u.getBookByName(bookName);
			if (u.failed == true) {
				failed = true;
			}
			
			Category theCategory = u.getCategoryByName(categoryName);
			if (u.failed == true) {
				failed = true;
			}
			
			if (failed == false) {
				theBook.category = theCategory;
				theCategory.booksInTheCategory.add(theBook);
				theCategory.quantity++;
				
				System.out.println("The book name: " + bookName + "added to category name: " + categoryName + "!"); 
			} else System.out.println("Failed because of the above reason!");
		}
		
		void addBookByIdToCategoryById(int bookId, int categoryId) {
			util u = new util();
			boolean failed = false;
			
			Book theBook = u.getBookById(bookId);
			if (u.failed == true) {
				failed = true;
			}
			
			Category theCategory = u.getCategoryById(categoryId);
			if (u.failed == true) {
				failed = true;
			}
			
			if (failed == false) {
				theBook.category = theCategory;
				theCategory.booksInTheCategory.add(theBook);
				theCategory.quantity++;
				
				System.out.println("The book id: " + bookId + "added to category id: " + categoryId + "!"); 
			} else System.out.println("Failed because of the above reason!");
		}
		
		void addBookByNameToCategoryById(String bookName, int categoryId) {
			util u = new util();
			boolean failed = false;
			
			Book theBook = u.getBookByName(bookName);
			if (u.failed == true) {
				failed = true;
			}
			
			Category theCategory = u.getCategoryById(categoryId);
			if (u.failed == true) {
				failed = true;
			}
			
			if (failed == false) {
				theBook.category = theCategory;
				theCategory.booksInTheCategory.add(theBook);
				theCategory.quantity++;
				
				System.out.println("The book name: " + bookName + "added to category id: " + categoryId + "!"); 
			} else System.out.println("Failed because of the above reason!");
		}
		
		void removeBookByIdFromCategoryByName(int bookId, String categoryName) {
			util u = new util();
			boolean failed = false;
			
			Book theBook = u.getBookById(bookId);
			if (u.failed == true) {
				failed = true;
			}
			
			Category theCategory = u.getCategoryByName(categoryName);
			if (u.failed == true) {
				failed = true;
			}
			
			if (failed == false) {
				theBook.category = null;
				theCategory.booksInTheCategory.remove(theBook);
				theCategory.quantity--;
				
				System.out.println("The book id: " + bookId + "removed from category name: " + categoryName + "!"); 
			} else System.out.println("Failed because of the above reason!");
		}
		
		void removeBookByNameFromCategoryByName(String bookName, String categoryName) {
			util u = new util();
			boolean failed = false;
			
			Book theBook = u.getBookByName(bookName);
			if (u.failed == true) {
				failed = true;
			}
			
			Category theCategory = u.getCategoryByName(categoryName);
			if (u.failed == true) {
				failed = true;
			}
			
			if (failed == false) {
				theBook.category = null;
				theCategory.booksInTheCategory.remove(theBook);
				theCategory.quantity--;
				
				System.out.println("The book name: " + bookName + "removed from category name: " + categoryName + "!"); 
			} else System.out.println("Failed because of the above reason!");
		}
		
		void removeBookByIdFromCategoryById(int bookId, int categoryId) {
			util u = new util();
			boolean failed = false;
			
			Book theBook = u.getBookById(bookId);
			if (u.failed == true) {
				failed = true;
			}
			
			Category theCategory = u.getCategoryById(categoryId);
			if (u.failed == true) {
				failed = true;
			}
			
			if (failed == false) {
				theBook.category = null;
				theCategory.booksInTheCategory.remove(theBook);
				theCategory.quantity--;
				
				System.out.println("The book id: " + bookId + "removed from category id: " + categoryId + "!"); 
			} else System.out.println("Failed because of the above reason!");
		}
		
		void removeBookByNameFromCategoryById(String bookName, int categoryId) {
			util u = new util();
			boolean failed = false;
			
			Book theBook = u.getBookByName(bookName);
			if (u.failed == true) {
				failed = true;
			}
			
			Category theCategory = u.getCategoryById(categoryId);
			if (u.failed == true) {
				failed = true;
			}
			
			if (failed == false) {
				theBook.category = null;
				theCategory.booksInTheCategory.remove(theBook);
				theCategory.quantity--;
				
				System.out.println("The book name: " + bookName + "removed from category id: " + categoryId + "!"); 
			} else System.out.println("Failed because of the above reason!");
		}

		void getMembers() {
			for (Member member: Storage.members) {
				System.out.print("name: " + member.name + " ");
				System.out.print("username: " + member.username + " ");
				System.out.print("password: " + member.password + " ");
				System.out.println();	
			}
		}
		
		void addMember(String name, String username, String password) {
			boolean alreadyExsited = false;
			for (Member member: Storage.members) {
				if (member.username == username) {
					alreadyExsited = true;
					break;
				}
			}
			
			if (alreadyExsited == false) {
				Member theMember = new Member();
				theMember.name = name;
				theMember.username = username;
				theMember.password = password;
				Storage.members.add(theMember);
				System.out.println("The member added!");
			} else System.out.println("The username is taken, try another one!");
		}
		
		void removeMember(String username) {
			boolean alreadyExsited = false;
			Member theMember = new Member();
			for (Member member: Storage.members) {
				if (member.username == username) {
					theMember = member;
					alreadyExsited = true;
					break;
				}
			}
			
			if (alreadyExsited == true) {
				Storage.members.remove(theMember);
				System.out.println("The member removed!");
			} else System.out.println("The member not found!");
		}

		void takeBookBack(int memberId, int bookId) {
			util u = new util();
			u.getBookById(bookId).lended = false;
			u.getBookById(bookId).borrowerId = "null";
			Storage.borrowingTime.remove(u.getBookById(bookId));
		}
		
		void getDonatedBooks() {
			util u = new util();
			for (Book donatedBook: u.getCategoryById(-2).booksInTheCategory) {
				System.out.print("id: " + donatedBook.id + " ");
				System.out.print("name: " + donatedBook.name + " ");
				System.out.print("author: " + donatedBook.author + " ");
				System.out.print("publisher: " + donatedBook.publisher + " ");
				System.out.print("yearOfPublication: " + donatedBook.yearOfPublication + " ");
				System.out.print("category: " + donatedBook.category.name + " ");
				System.out.print("is lended: " + donatedBook.lended + " ");
				System.out.println();	
			}
		}
		
		void addDonatedBook(int id) {
			util u = new util();
			boolean found = false;
			for (Book donatedBook: u.getCategoryById(-2).booksInTheCategory) {
				if (donatedBook.id == id) {
					donatedBook.category = null;
					Storage.books.add(donatedBook);
					u.getCategoryById(-2).booksInTheCategory.remove(donatedBook);
					u.getCategoryById(-2).quantity--;
					found = true;
					System.out.println("The donated book has been moved to available books!");
					
					break;
				}
			}
			
			if (found == false) System.out.println("The book not found!");
		}
		
	}

	static class Member {
		private String name;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}

		private String username;
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}

		private String password;
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}

		void getBooks() {
			for (Book book: Storage.books) {
				System.out.print("id: " + book.id + " ");
				System.out.print("name: " + book.name + " ");
				System.out.print("author: " + book.author + " ");
				System.out.print("publisher: " + book.publisher + " ");
				System.out.print("yearOfPublication: " + book.yearOfPublication + " ");
				System.out.print("category: " + book.category.name + " ");
				System.out.print("is lended: " + book.lended + " ");
				System.out.println();			
			}
		}
		
		void searchBookById(int id) {
			util u = new util();
			Book theBook = u.getBookById(id);
			if (theBook != null) {
				System.out.print("id: " + theBook.id + " ");
				System.out.print("name: " + theBook.name + " ");
				System.out.print("author: " + theBook.author + " ");
				System.out.print("publisher: " + theBook.publisher + " ");
				System.out.print("yearOfPublication: " + theBook.yearOfPublication + " ");
				System.out.print("category: " + theBook.category.name + " ");
				System.out.print("is lended: " + theBook.lended + " ");
				System.out.println();	
			}
		}
		
		void searchBookByName(String name) {
			util u = new util();
			Book theBook = u.getBookByName(name);
			if (theBook != null) {
				System.out.print("id: " + theBook.id + " ");
				System.out.print("name: " + theBook.name + " ");
				System.out.print("author: " + theBook.author + " ");
				System.out.print("publisher: " + theBook.publisher + " ");
				System.out.print("yearOfPublication: " + theBook.yearOfPublication + " ");
				System.out.print("category: " + theBook.category.name + " ");
				System.out.print("is lended: " + theBook.lended + " ");
				System.out.println();	
			}
		}
		
		void BorrowBookById(int id) {
			util u = new util();
			Book theBook = u.getBookById(id);
			if (theBook != null) {
				theBook.lended = true;
				theBook.borrowerId = this.username;
				Storage.borrowingTime.put(theBook, System.currentTimeMillis());
			}
		}

		void getLendedBooks() {
			util u = new util();
			
			for (Book book: Storage.borrowingTime.keySet()) {
				System.out.print("id: " + book.id + " ");
				System.out.print("name: " + book.name + " ");
				System.out.print("author: " + book.author + " ");
				System.out.print("publisher: " + book.publisher + " ");
				System.out.print("yearOfPublication: " + book.yearOfPublication + " ");
				System.out.print("category: " + book.category.name + " ");
				System.out.print("borrowed time: " + u.getPassedTime(Storage.borrowingTime.get(book)) + " ");
				System.out.println();	
			}
		}

		void donate(int id, String name, String author, String publisher, int yearOfPublication, String category) {
			util u = new util();
			boolean alreadyExisted = false;
			
			for (Book donatedBook: u.getCategoryById(-2).booksInTheCategory) {
				if (donatedBook.id == id) {
					alreadyExisted = true;
				}
			}
			
			if (alreadyExisted == false) {
				Book theBook = new Book();
				theBook.id = id;
				theBook.name = name;
				theBook.author = author;
				theBook.publisher = publisher;
				theBook.yearOfPublication = yearOfPublication;
				theBook.category = u.getCategoryByName(category);
				u.getCategoryById(-2).booksInTheCategory.add(theBook);
				u.getCategoryById(-2).quantity++;
				
				System.out.println("The book was donated successfully!");
			} else System.out.println("The book already is in the donation list!");
		}
	}
}

public class Library {
	public static void managerHelp() {
		
	}
	
	public static void managerDoes(String task) {
		
	}
	
	private static void memberHelp() {
		
	}
	
	public static void memberDoes(App.Member theMember, String task) {
		
	}
	
	public static void signUp() {
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Enter Your Name -> ");
		String name = sc.nextLine();
		
		App.util u = new App.util();
		String username;
		System.out.print("Enter Your Username -> ");
		do {
			username = sc.next();
		} while (u.usernameCheck(username, "signUp"));
		
		System.out.print("Enter Your Password -> ");
		String password = sc.next();
		
		App.Manager m = new App.Manager();
		m.addMember(name, username, password);
		
		sc.close();
	}
	
	public static void signIn() {
		Scanner sc = new Scanner(System.in);
		
		App.util u = new App.util();
		String username;
		System.out.print("Enter Your Username -> ");
		do {
			username = sc.next();
		} while (!u.usernameCheck(username, "signIn") || username.equals(App.Storage.manager.getUsername()));
		
		System.out.print("Enter Your Password -> ");
		String password = sc.next();
		
		if (username.equals(App.Storage.manager.getUsername())) {
			if (password.equals(App.Storage.manager.getPassword())) {
				managerHelp();
				System.out.print("Enter The Quantity Of Your Tasks -> ");
				int n = Integer.parseInt(sc.next());
				for (int i = 0; i < n; i++) {
					String task = sc.next(); 
					managerDoes(task);
				}
			} else System.out.print("The Password Is Not Correct!");
		} else {
			for (App.Member member: App.Storage.members) {
				if (username.equals(member.getUsername())) {
					if (password.equals(member.getPassword())) {
						memberHelp();
						System.out.print("Enter The Quantity Of Your Tasks -> ");
						int n = Integer.parseInt(sc.next());
						for (int i = 0; i < n; i++) {
							String task = sc.next(); 
							memberDoes(member, task);
						}
					} else System.out.print("The Password Is Not Correct!");
				}
			}
		}
		System.out.println("Tasks Are Done!");
		
		sc.close();
	}

	public static void main(String[] args) throws IOException {
		
		//App.Storage.load();
		
		Scanner sc = new Scanner(System.in);
		
		int running = 1;
		int sui;
		
		do {
			System.out.print("Wellcome!\nFor Signing Up Enter 1 : For Signing In Enter 2\n-> ");
			
			sui = Integer.parseInt(sc.next());
			while (sui != 1 && sui != 2) {
				System.out.print("Enter 1 or 2.\n-> ");
				sui = Integer.parseInt(sc.next());
			}
			
			if (sui == 1) {
				System.out.println("Signing Up...");
				signUp();
			} else if (sui == 2) {
				System.out.println("Signing In...");
				signIn();
			}
			
			System.out.print("For Exit The App Enter 0 : For Continue Enter 1\n-> ");
			running = Integer.parseInt(sc.next());
		} while(running == 1);
		System.out.println("Exited!");
		
		//App.Storage.save();
		
		sc.close();
	}
}
