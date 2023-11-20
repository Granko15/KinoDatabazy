package UI;
import app.DBContext;
import entity.*;

import java.awt.print.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.sql.Time;
import java.util.*;

public class Menu {

    private static final String[] options = {
            "| CUSTOMER",
            "|--1: Show all customers",
            "|--2: Show a customer",
            "|--3: Add a customer",
            "|--4: Delete a customer",
            "|--5: Edit a customer",
            "|----------------------------",
            "| BOOKING",
            "|--6: Get booking details",
            "|--7: Get customer bookings",
            "|----------------------------",
            "| MOVIE",
            "|--8: Show all movies",
            "|--9: Show film details",
            "|-10: Edit film title",
            "|-11: Edit film genres",
            "|-12: Add new film",
            "|-13: Delete film",
            "|-14: Top monthly",
            "|----------------------------",
            "| COUPON",
            "|-15: Get customer coupons",
            "|-16: Add a coupon",
            "|-17: Mark as used",
            "|-18: Delete coupon",
            "|----------------------------",
            "| SHOWING",
            "|-19: Add a showing",
            "|-20: Cancel a showing",
            "|-21: Get showing",
            "|----------------------------",
            "| ZLOZITEJSIE",
            "|-22: Create booking",
            "|-23: Confirm booking",
            "|-24: Cancel unconfirmed bookings",
            "|-25: Compensate",
            "|-26: Use a coupon",
            "|-27: Last month top",
            "|-28: Exit",
    };

    public static void printMenu() {
        System.out.println("\n__________________");
        for (String option : options) {
            System.out.println(option);
        }
        System.out.print("Choose your option : ");

    }

    /** Customer*/
    public void getCustomers() throws SQLException { /** Show all customers details*/
        for (Customer c : CustomerGetter.getInstance().findAll()) {
            System.out.println(c);
            System.out.println();
        }
    }

    public void getCustomerById() throws IOException, SQLException { /** Show customer details, get Customer by ID*/
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter customer id:");
        int customerId = Integer.parseInt(r.readLine());

        Customer customer = CustomerGetter.getInstance().getById(customerId);

        if (customer == null) {
            System.out.println("Customer with this ID does not exist!");
            return;
        }
        System.out.println(customer);
    }

    public void deleteCustomer() throws IOException, SQLException { /** Delete Customer with this ID*/
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter a customer's id:");
        int customerId = Integer.parseInt(r.readLine());

        Customer customer = CustomerGetter.getInstance().getById(customerId);

        if (customer == null) {
            System.out.println("No such customer exists");
        } else {
            customer.delete();
            System.out.println("The customer has been successfully deleted");
        }
    }

    private void addACustomer() throws IOException, SQLException { /** Add a new customer*/
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Customer customer = new Customer();

        System.out.println("Enter first name:");
        customer.setfName(br.readLine());
        System.out.println("Enter last name:");
        customer.setlName(br.readLine());
        System.out.println("Enter e-mail:");
        customer.setMail(br.readLine());
        System.out.println("Enter your age:");
        customer.setAge(Integer.parseInt(br.readLine()));
        System.out.println("Enter your date of birth in format(YYYY-MM-DD):");
        customer.setDateOfBirth(br.readLine());

        customer.insert();

        System.out.println("The customer added sucessfully");
        System.out.print("The customer's id is: ");
        System.out.println(customer.getId());
    }

    private void editACustomer() throws IOException, SQLException {/** Edit customer with this ID*/
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter a customer's id:");
        int customerId = Integer.parseInt(br.readLine());

        Customer customer = CustomerGetter.getInstance().getById(customerId);

        if (customer == null) {
            System.out.println("No such customer exists");
        } else {
            System.out.println(customer);

            System.out.println("Enter first name:");
            customer.setfName(br.readLine());
            System.out.println("Enter last name:");
            customer.setlName(br.readLine());
            System.out.println("Enter e-mail:");
            customer.setMail(br.readLine());
            System.out.println("Enter your age:");
            customer.setAge(Integer.parseInt(br.readLine()));
            System.out.println("Enter your date of birth in format(YYYY-MM-DD):");
            customer.setDateOfBirth(br.readLine());

            customer.update();

            System.out.println("The customer has been successfully updated");
        }
    }

    /** Movie*/
    public void getMovies() throws SQLException { /** Show details of all Movies*/
        for (Film f : FilmGetter.getInstance().findAll()) {
            System.out.println(f);
            System.out.println();
        }
    }

    public void getMovieById() throws IOException, SQLException { /** Show details of a Movie with this ID and list all MovieSHowings for this Movie*/
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter movie id:");
        int movieId = Integer.parseInt(r.readLine());

        Film f = FilmGetter.getInstance().getById(movieId);

        if (f == null) {
            System.out.println("Movie with this ID does not exist!");
            return;
        }

        List<MovieShowing> m = MovieShowingGetter.getInstance().getShowingsOfFilm(movieId);

        if (m == null) {
            System.out.println("Film with this ID does not have any showings!");
            return;
        }
        System.out.println("\nMovie with this ID:");
        System.out.println(f);
        System.out.println("\nMovie showings available:");
        System.out.println("-------------------------");
        for (MovieShowing movie : m){
            System.out.println(movie);
            System.out.println("########################################");
        }
    }

    public void editMovieTitle() throws IOException, SQLException { /** Edit title of Movie with this ID*/
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter a movie id:");
        int movieId = Integer.parseInt(br.readLine());

        Film f = FilmGetter.getInstance().getById(movieId);

        if (f == null) {
            System.out.println("No such movie exists");
        } else {
            System.out.println(f);
            System.out.println("Enter Title:");
            f.setTitle(br.readLine());

            f.update();

            System.out.println("The title has been changed successfully!");
        }
    }

    public void editMovieCat() throws IOException, SQLException{ /** Update genres of Movie with this ID*/
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Film f = new Film();
        System.out.println("Enter film ID to edit:");
        f.setId(Integer.valueOf(br.readLine()));

        Set<Integer> catIds = new HashSet<>();
        System.out.println("Enter one category id at a time\nor press 'x' to continue:");

        while (true){
            String add = br.readLine();
            if (add.equals("x")) break;
            catIds.add(Integer.valueOf(add));
        }

        for (Integer id : catIds){
            System.out.println(id);
            Category category = new Category();
            category.setId(id);
            category.insertMovCat(f.getId());
        }

        System.out.println("Categories added successfully!");
        System.out.println(FilmGetter.getInstance().getById(f.getId()));

    }

    public void addAMovie() throws IOException,SQLException{ /** Add a new Movie*/
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Film film = new Film();
        Category category = new Category();

        System.out.println("Enter movie title:");
        film.setTitle(br.readLine());
        System.out.println("Enter duration in minutes:");
        film.setMinutes(Integer.valueOf(br.readLine()));
        System.out.println("Enter rating 0-100:");
        film.setRating(Integer.valueOf(br.readLine()));
        System.out.println("Enter description:");
        film.setDescription(br.readLine());
        System.out.println("Enter year in format(YYYY-MM-DD):");
        film.setYear(br.readLine());

        System.out.println("Enter category id:");
        category.setId(Integer.valueOf(br.readLine()));

        film.insert();
        category.insertMovCat(film.getId());

        System.out.println("The film added sucessfully");
        System.out.print("Film's id is:");
        System.out.println(film.getId());
    }

    public void deleteMovie() throws IOException, SQLException {  /** Delete a Movie with this ID*/
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Enter a movie id:");
        int movieId = Integer.parseInt(r.readLine());

        Film f = FilmGetter.getInstance().getById(movieId);

        if (f == null) {
            System.out.println("No such movie exists");
        } else {
            f.delete();
            System.out.println("The movie has been successfully deleted");
        }
    }

    public static void topMovies() throws IOException, SQLException{  /** List the most grossing Movies for each Category over the last 6 months*/
        MovieStatisticGetter.getInstance().findAll();
    }

    /** Booking*/
    public static void customerBookingDetails() throws IOException, SQLException {  /** Get all Customer Booking details*/
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter customer id:");
        int bookingId = Integer.parseInt(r.readLine());

        List<Booking> b = BookingGetter.getInstance().getByCustomerId(bookingId);

        if (b.isEmpty()) {
            System.out.println("Customer with this ID does not have bookings!");
            return;
        }
        for (Booking bb : b){
            System.out.println(bb+"\n");
        }
    }

    public static void getBooking() throws IOException, SQLException {  /** Get Booking deatils with this ID*/
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter booking id:");
        int bookingId = Integer.parseInt(r.readLine());

        Booking b = BookingGetter.getInstance().getById(bookingId);

        if (b == null) {
            System.out.println("Booking with this ID does not exist!");
            return;
        }
        System.out.println(b);
    }
    /** Coupon*/

    public static void customerCoupons() throws IOException, SQLException{   /** Show all Customer Coupons with this ID*/
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter customer id:");
        int customerId = Integer.parseInt(r.readLine());

        List<Coupon> c = CouponGetter.getInstance().getCustomerCoupons(customerId);

        if (c.isEmpty()) {
            System.out.println("This customer does not have coupons!");
            return;
        }
        for (Coupon cc : c){
            System.out.println(cc+"\n");
        }
    }

    public void addCoupon() throws IOException,SQLException { /** Add a new Coupon*/
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Coupon coupon = new Coupon();

        System.out.println("Enter customer id:");
        coupon.setCustomerId(Integer.valueOf(br.readLine()));
        System.out.println("Enter coupon valid date:");
        coupon.setValidUntil(br.readLine());
        System.out.println("Enter coupon value");
        coupon.setCouponValue(Integer.valueOf(br.readLine()));
        System.out.println("Enter coupon CODE");
        coupon.setCouponText(br.readLine());

        coupon.insert();

        System.out.println("The coupon added sucessfully");
        System.out.print("Coupons's id is:");
        System.out.println(coupon.getId());
    }

    public void markAsUsed() throws IOException, SQLException{ /** Mark Coupon with this ID as USED*/

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Coupon coupon = new Coupon();

        System.out.println("Enter coupon id:");
        coupon.setId(Integer.valueOf(br.readLine()));

        coupon.updateUse();
        System.out.println("The coupon was marked as used");
    }

    public void deleteCoupon() throws IOException, SQLException{ /** Delete Coupon with this ID*/

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Coupon coupon = new Coupon();

        System.out.println("Enter coupon id:");
        coupon.setId(Integer.valueOf(br.readLine()));

        coupon.delete();
        System.out.println("The coupon removed sucessfully");
    }

    /** Showings*/

    public static void addShowing() throws IOException, SQLException{   /** Add a new MovieShowing for a Movie*/
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        MovieShowing showing = new MovieShowing();

        System.out.println("Enter cinema hall ID:");
        showing.setCinemaHallId(Integer.valueOf(br.readLine()));
        System.out.println("Enter movie ID:");
        showing.setMovieId(Integer.valueOf(br.readLine()));
        System.out.println("Enter time of showing:");
        showing.setShowingTime(br.readLine());
        System.out.println("Enter date of showing:");
        showing.setShowingDate(br.readLine());

        showing.insert();
        System.out.println("The showing added sucessfully");
        System.out.print("Showing's id is:");
        System.out.println(showing.getId());
    }

    public static void cancelShowing() throws IOException, SQLException { /** Cancel MovieShowing with this ID*/
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        MovieShowing showing = new MovieShowing();
        System.out.println("Enter showing ID to cancel:");
        showing.setId(Integer.valueOf(br.readLine()));

        showing.delete();
        System.out.println("Showing canceld successfully");
        System.out.println(showing);

    }

    public static void getShowing() throws IOException,SQLException{ /** Show MovieShowing details with this ID*/
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter showing id:");
        int showingId = Integer.parseInt(r.readLine());

        MovieShowing m = MovieShowingGetter.getInstance().getById(showingId);

        if (m == null) {
            System.out.println("Showing with this ID does not exist!");
            return;
        }
        System.out.println(m);
    }

    /** ZLOZITEJSIE*/

    public static void useCoupon() throws IOException, SQLException{ /** Get Customer Coupon and use it for lowering the price of Booking*/
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

        /** Get Customer ID, Booking ID, Coupon Code*/
        System.out.println("Enter your ID:");
        int customerId = Integer.parseInt(r.readLine());
        Customer c = CustomerGetter.getInstance().getById(customerId);

        System.out.println("Enter booking ID:");
        int bookingId = Integer.parseInt(r.readLine());
        Booking b = BookingGetter.getInstance().getById(bookingId);

        System.out.println("Enter your coupon code:");
        String couponCode = r.readLine();

        Coupon coupon = CouponGetter.getInstance().getCouponByTextAndCustomer(c.getId(),couponCode);

        if(coupon == null) {
            System.out.println("You don't have this coupon");
        }

        /** Update Coupon use, Change Booking Price*/
        Connection con = DBContext.getConnection();
        con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        con.setAutoCommit(false);
        try {

            b.apllyDiscount(coupon,c);
            assert coupon != null;
            coupon.updateUse();
            b = BookingGetter.getInstance().getById(bookingId);
            System.out.println("Discount applied successfully");
            System.out.println(b);

            con.commit();
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }

    }

    public static void createBooking() throws IOException, SQLException{    /** Create a new Booking,*/
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

        int customerId;
        int numberOfAdultTickets;
        int numberOfDiscountTickets;
        int movieShowingId;

        System.out.println("Enter your customer ID:");
        customerId = Integer.parseInt(r.readLine());

        System.out.println("Enter movie showing ID:");
        movieShowingId = Integer.parseInt(r.readLine());
        MovieShowing showing = MovieShowingGetter.getInstance().getById(movieShowingId);

        /** check 40 minutes before Showing */
        Time showTime = Time.valueOf(showing.getShowingTime().substring(0,8));
        long millis=System.currentTimeMillis();
        Time time = new Time(millis);
        long c = time.getTime() - showTime.getTime();
        Time diff = new Time(c);
        Time boundary = Time.valueOf("00:40:00");

        if (diff.getTime() < boundary.getTime()){
            System.out.println("The movie starts in less than 40 minutes!");
            return;
        }

        System.out.println("Enter number of ADULT tickets:");
        numberOfAdultTickets = Integer.parseInt(r.readLine());

        System.out.println("Enter number of DISCOUNT tickets:");
        numberOfDiscountTickets = Integer.parseInt(r.readLine());
        int ticketsTotal = numberOfAdultTickets + numberOfDiscountTickets;

        /** Check for free Seats in this Showing*/

        if(!MovieShowingGetter.getInstance().checkFreeSeats(ticketsTotal, showing.getId())){
            System.out.println("Not enough free seats for this showing!");
            return;
        };

        Booking booking = new Booking();
        /** For each Adult and Discount Ticket Add a new Ticket */
        Connection con = DBContext.getConnection();
        con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        con.setAutoCommit(false);
        try {
            booking.setCustomerId(customerId);
            booking.setBookingPrice((numberOfAdultTickets*8) + (numberOfDiscountTickets*5));
            booking.setMovieTitle(showing.getMovieName());
            booking.setBookingDate(showing.getShowingDate());
            booking.setBookingTime(showing.getShowingTime());
            booking.createReservation();

            for (int i = 0; i < numberOfAdultTickets; i++){
                Ticket t = new Ticket();
                t.setBookingId(booking.getId());
                HallSeat h = HallSeatGetter.getInstance().getFreeSeat();
                t.setHallSeatId(h.getId());
                t.setMovieId(showing.getMovieId());
                t.setMovieShowing(showing.getId());
                t.setMovieName(showing.getMovieName());
                t.setTicketPrice(8);
                t.setTicketType("basic");
                t.insert();
            }

            for (int i = 0; i < numberOfDiscountTickets; i++){
                Ticket t = new Ticket();
                t.setBookingId(booking.getId());
                HallSeat h = HallSeatGetter.getInstance().getFreeSeat();
                t.setHallSeatId(h.getId());
                t.setMovieId(showing.getMovieId());
                t.setMovieShowing(showing.getId());
                t.setMovieName(showing.getMovieName());
                t.setTicketPrice(5);
                t.setTicketType("discount");
                t.insert();
            }
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }

        System.out.println("Booking has been created, it will be removed after 10 minutes if not paid");
        System.out.println(BookingGetter.getInstance().getById(booking.getId()));

    }

    public static void confirmBooking() throws IOException, SQLException{   /** Confirm Booking if less than 10 minutes since creation*/
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        int bookingId;
        System.out.println("Enter booking ID you wish to confirm");
        bookingId = Integer.parseInt(r.readLine());

        Booking booking = BookingGetter.getInstance().getById(bookingId);
        if (booking.getSold()){
            System.out.println("Booking was already confirmed");
            return;
        }

        /** Check 10 minutes boundary*/
        Time createdTime = Time.valueOf(booking.getCreatedTime().substring(0,8));
        long millis = System.currentTimeMillis();
        Time time = new Time(millis);
        long c = time.getTime() - createdTime.getTime();
        Time diff = new Time(c);
        System.out.println(createdTime);
        System.out.println(time);
        System.out.println(diff);

        System.out.println(Integer.parseInt(diff.toString().substring(0,2)));
        System.out.println(Integer.parseInt(diff.toString().substring(3,5)));

        Connection con = DBContext.getConnection();
        con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        con.setAutoCommit(false);
        try {
            /** If more than 10 minutes passed fre the Seats and delete the Tickets*/
            if (Integer.parseInt(diff.toString().substring(0,2)) > 1 ||
                    Integer.parseInt(diff.toString().substring(3,5)) > 10 ){
                System.out.println("10 minutes have passed, please create a new booking!");
                booking.delete();
                List<Ticket> tickets = TicketGetter.getInstance().getTicketsByBooking(booking.getId());
                for(Ticket t : tickets){
                    t.freeTheSeat();
                    t.delete();
                }
                return;
            }
            booking.confirmBooking();
            System.out.println("Booking confirmed successfully");
            System.out.println(booking);
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
    }

    public static void freeUnconfirmedBookings() throws IOException, SQLException{ /** Free and cancel all unconfirmed Bookings older than minutes*/
        List<Booking> toDelete = BookingGetter.getInstance().getUnconfirmedBookings();
        Connection con = DBContext.getConnection();
        con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        con.setAutoCommit(false);
        try {
            for (Booking b : toDelete){
                if (b.getId() == 0) continue;
                List<Ticket> tickets = TicketGetter.getInstance().getTicketsByBooking(b.getId());
                for(Ticket t : tickets){
                    t.freeTheSeat();
                    t.delete();
                }
                b.delete();
            }
        } catch (Exception e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
        }
        System.out.println("Bookings canceled successfully");
    }

    /** STATISTIKA*/

    public static void getLastMonthBest() throws IOException, SQLException{ /** Get last month earnings for each Category and compare with previous mont*/
        MovieStatisticGetter.getLastMonth();
    }

    /** RUN*/
    public static void exit() {
        System.out.println("That's about it, see ya!");
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        int option = 1;
        while (option != options.length) {
            printMenu();
            try {
                option = scanner.nextInt();
                if (option < 1 || option > options.length) {
                    System.out.println("Please enter a value between 1 and " + options.length);
                    scanner.next();
                }
                switch (option) {
                    case 1 -> getCustomers();
                    case 2 -> getCustomerById();
                    case 3 -> addACustomer();
                    case 4 -> deleteCustomer();
                    case 5 -> editACustomer();
                    case 6 -> getBooking();
                    case 7 -> customerBookingDetails();
                    case 8 -> getMovies();
                    case 9 -> getMovieById();
                    case 10 -> editMovieTitle();
                    case 11 -> editMovieCat();
                    case 12 -> addAMovie();
                    case 13 -> deleteMovie();
                    case 14 -> topMovies();
                    case 15 -> customerCoupons();
                    case 16 -> addCoupon();
                    case 17 -> markAsUsed();
                    case 18 -> deleteCoupon();
                    case 19 -> addShowing();
                    case 20 -> cancelShowing();
                    case 21 -> getShowing();
                    case 22 -> createBooking();
                    case 23 -> confirmBooking();
                    case 24 -> freeUnconfirmedBookings();
                    case 25 -> System.out.println("Nevedel som ako na to uplne :^) ");
                    case 26 -> useCoupon();
                    case 27 -> getLastMonthBest();
                    case 28 -> exit();
                }
           } catch (Exception ex) {
                System.out.println("An unexpected error happened. Please try again");
                scanner.next();
           }
        }
    }
}


