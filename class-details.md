# Class Details
### App
- public static void main(String[]) -- get spring application context, run controller

### data.DataException
Custom data layer exception.

- public DataException(String, Throwable) -- constructor, Throwable arg is the root cause exception

### data.ReservationFileRepository
Supports all CRUD operations.
- private String filePath
- public List<Reservation> findForHost(Host) -- finds all Reservations at a host location
- public List<Reservation> findForGuestAndHost(Host, Guest) -- finds all Reservations by a guest at a host location
- public Reservation add(Reservation) -- create a Reservation
- public boolean update(Reservation) -- updates/edits a Reservation
- public boolean delete(Reservation) -- cancels a Reservation
- private String serialize(Reservation) -- convert a Reservation into a String (a line) in the file
- private Reservation deserialize(String) -- convert a String into a Reservation
- private void writeAll(List<Reservation>, Host) -- writes all reservations for a host to the host's reservations file

### data.ReservationRepository (interface)
Contract for ReservationFileRepository and ReservationRepositoryTestDouble.

- List<Reservation> findForHost(Host)
- List<Reservation> findForGuestAndHost(Host, Guest)
- Reservation add(Reservation)
- boolean update(Reservation)
- boolean delete(Reservation)

### data.HostFileRepository
Supports only Read operation.
- private String filePath
- public Host findHostByEmail(String) -- finds Host by email address
- private Host deserialize(String) -- convert a String into a Host

### data.HostRepository (interface)
Contract for HostFileRepository and HostRepositoryTestDouble.

- Host findHostByEmail(String)

### data.GuestFileRepository
Supports only Read operation.
- private String filePath
- public Guest findGuestByEmail(String) -- finds Guest by email address
- private Guest deserialize(String) -- convert a String into a Guest

### data.GuestRepository (interface)
Contract for GuestFileRepository and GuestRepositoryTestDouble.

- Host findGuestByEmail(String)

### domain.Result
- private ArrayList<String> messages -- error messages
- private Reservation payload -- an optional Reservation payload
- public boolean isSuccess() -- calculated getter, true if no error messages
- public List<String> getMessages() -- messages getter, create a new list
- public Reservation getPayload() -- Payload/Reservation getter
- public void setPayload(Reservation) -- Payload/Reservation setter
- public void addMessage(String) -- adds an error message to messages

### domain.ReservationService
- private ReservationRepository repository -- required data dependency
- public ReservationService(ReservationRepository) -- constructor
- public List<Reservation> findForHost(Host) -- pass-through to repository
- public List<Reservation> findForGuestAndHost(Host, Guest) -- pass-through to repository
- public Result add(Reservation) -- validate, then add via repository
- public Result update(Reservation) -- validate, then update via repository
- public Result delete(Reservation) -- pass-through to repository
- private Result validate(Reservation) -- general-purpose validation routine

### domain.HostService
- private HostRepository repository -- required data dependency
- public HostService(HostRepository) -- constructor
- public Host findByEmail(String) -- pass-through to repository

### domain.GuestService
- private GuestRepository repository -- required data dependency
- public GuestService(GuestRepository) -- constructor
- public Guest findByEmail(String) -- pass-through to repository

### models.Guest
- private int guestId
- private String firstName
- private String lastName
- private String email
- private String phone
- private String state
- Full getters and setters
- override equals and hashCode

### models.Host
- private String hostId
- private String lastName
- private String email
- private String phone
- private String address
- private String city
- private String state
- private String postalCode
- private BigDecimal standardRate
- private BigDecimal weekendRate
- Full getters and setters
- override equals and hashCode

### models.Reservation
- private int reservationId
- private String hostId
- private int guestId
- private LocalDate startDate
- private LocalDate endDate
- private BigDecimal total
- Full getters and setters
- override equals and hashCode

### ui.ConsoleIO
- private static final String NUMBER_OUT_OF_RANGE
- private static final String REQUIRED
- private static final String INVALID_DATE
- private final Scanner scanner -- a Scanner to be used across all methods
- private DateTimeFormatter formatter -- sets a date format/pattern
- public void print(String)
- public void println(String)
- public void printf(String, Object...)
- public String readString(String)
- public String readRequiredString(String)
- public int readInt(String)
- public int readInt(String, int, int) -- reads integer with min and max range
- public boolean readBoolean(String)
- public LocalDate readLocalDate(String)

### ui.MainMenuOption
Enum with all menu options
0. Exit
1. View Reservations for Host
2. Make a Reservation
3. Edit a Reservation
4. Cancel a Reservation

### ui.Controller
- private View view -- required View dependency
- private ReservationService reservationService -- required reservation service dependency
- private GuestService guestService -- required guest service dependency
- private HostService hostService -- required host service dependency
- public Controller(View, ReservationService, GuestService, HostService) -- constructor with dependencies
- public void run() -- kicks off the whole app
- private void runAppLoop -- runs menu loop
- private void viewReservationsForHost() -- coordinates between reservationService and view to display reservations for a Host
- private void addReservation() -- coordinates between reservationService and view to add a new reservation
- private void updateReservation() -- coordinates between reservationService and view to update a reservation
- private void deleteReservation() -- coordinates between reservationService and view to delete a reservation
- private Guest getGuest() -- coordinates between guestService and view to find a Guest
- private Host getHost() -- coordinates between hostService and view to find a Host

### ui.View
- private final ConsoleIO io -- required ConsoleIO dependency
- public View(ConsoleIO) -- constructor with dependency
- public MainMenuOption selectMainMenuOption() -- display the main menu and select an option
- public void printHeader(String) -- display text to the console with emphasis
- public void printResult(Result) -- display a Result with either success or failure w/ messages included
- public void printReservations(List<Reservations>) -- display reservations in a section with the section's name
- public Reservation chooseReservation(List<Reservations>) -- choose a reservation from a list of options (useful for update and delete)
- public Reservation makeReservation() -- make a reservation from scratch
- public Reservation update(Reservation) -- accept an existing Reservation, update it, and return it
- public void printHostNameAndAddress -- displays the host's last name and address
- public void printSummary(Reservation) -- displays start_date, end_date and total fee of reservation
- public void enterToContinue() -- prompts user to press Enter key to continue
