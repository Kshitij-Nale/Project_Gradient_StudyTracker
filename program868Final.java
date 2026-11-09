//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//   Package Files Inclusion/Imports
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    import java.io.*;
    import java.util.*;
    import java.time.LocalDate;
    import javax.swing.*;
    import java.awt.*;


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  Author      :   Kshitij Nale
//  Date        :   1/11/2026
//  Class Name  :   StudyLog
//  Description :   Data model that represents a single study session entry. It stores the 
//                  date, subject, duration and a short description of what was studied.
//
//  Responsibilities :
//      - Hold the details of one study session
//      - Provide read-only access to its fields through getters
//      - Provide a readable text form of the log through toString()
//      - Support value-based comparison through equals() and hashCode(),
//        so a log can be located and removed from a collection
//
//  Attributes :
//      Date         - Date on which the study session was recorded
//      Subject      - Subject or technology studied (C, C++, Java, etc.)
//      Duration     - Time spent studying, in hours
//      Description  - Short note about the topics covered
//
//
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    class StudyLog
    {
        private LocalDate Date;
        private String Subject;
        private double Duration;
        private String Description;

        public StudyLog(LocalDate a, String b, double c, String d)
        {
            this.Date = a;
            this.Subject = b;
            this.Duration = c;
            this.Description = d;
        }

        @Override
        public String toString()
        {
            return Date + " | " + Subject + " | " + Duration + " | " + Description;
        }

        public LocalDate getDate()
        {
            return this.Date;
        }

        public String getSubject()
        {
            return this.Subject;
        }

        public double getDuration()
        {
            return this.Duration;
        }

        public String getDescription()
        {
            return this.Description;
        }

        @Override
        public boolean equals(Object obj)
        {
            if(this == obj) return true;
            if(obj == null || getClass() != obj.getClass()) return false;

            StudyLog other = (StudyLog) obj;

            return this.Date.equals(other.Date)
                && this.Subject.equals(other.Subject)
                && Double.compare(this.Duration, other.Duration) == 0
                && this.Description.equals(other.Description);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(Date, Subject, Duration, Description);
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  Author      :   Kshitij Nale
//  Date        :   01/11/2026
//  Class Name  :   StudyTracker
//  Description :   Core logic class of the application. It stores all study logs and provides operations to insert, delete, 
//                  display, summarize (by date and by subject), and export/load logs to and from CSV files. 
//                  It supports both the console menu and the GUI.
//
//  StudyTracker(Scanner)  : Constructor that creates the empty database and stores the shared scanner.
//
// ---- Console methods ----
//  InsertLog()            : Reads log details from the console and adds a new log.
//  DeleteLog()            : Reads log details from the console and removes the matching log.
//  DisplayLog()           : Prints all study logs to the console.
//  ExportToCSV()          : Asks for a file name in the console and saves all logs to a CSV file.
//  SummaryByDate()        : Prints the total study hours for each date.
//  SummaryBySubject()     : Prints the total study hours for each subject.
//  LoadFromCSV()          : Asks for a file name in the console and loads logs from a CSV file.
//
// ---- GUI methods ----
//  InsertLog(sub,dur,desc): Validates the given values, adds a new log, and returns a status message.
//  DeleteLog(sub,dur,desc): Validates the given values, removes the matching log, and returns a status message.
//  GetLogs()              : Returns the list of all study logs.
//  GetSummaryByDate()     : Returns a sorted map of total study hours per date.
//  GetSummaryBySubject()  : Returns a sorted map of total study hours per subject.
//  ExportToCSV(fileName)  : Saves all logs to the given CSV file and returns a status message.
//  LoadFromCSV(fileName)  : Loads logs from the given CSV file and returns a loaded/skipped count message.
//
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    class StudyTracker
    {
        private ArrayList<StudyLog> Database;

        private Scanner sc;

        public StudyTracker(Scanner sc)
        {
            this.Database = new ArrayList<StudyLog>();
            this.sc = sc;
        }

        // ======================================================
        //  PART A : Console Methods
        // ======================================================

        // ---------------- 1 : Insert ----------------
        public void InsertLog()
        {
            System.out.println("-----------------------------------------");
            System.out.println("---- Enter the details of your study ----");
            System.out.println("-----------------------------------------");

            LocalDate lobj = LocalDate.now();
            System.out.println("We are entering the date as : " + lobj);

            System.out.println("Enter the name of subject like C/C++/Java etc");
            String sub = sc.nextLine();

            System.out.println("Enter the time period of your study (hours) : ");
            double dur;

            try
            {
                dur = sc.nextDouble();
            }
            catch(InputMismatchException e)
            {
                // discard bad input
                sc.nextLine();

                System.out.println("Invalid duration. Log not inserted.");
                return;
            }

            sc.nextLine();

            if(dur <= 0)
            {
                System.out.println("Duration must be greater than 0. Log not inserted.");
                return;
            }

            System.out.println("Please provide the description of your study : ");
            String desc = sc.nextLine();

            Database.add(new StudyLog(lobj, sub, dur, desc));

            System.out.println("Study log gets inserted successfully");
            System.out.println("-----------------------------------------");
        }

        public String DeleteLog(String sub, String durText, String desc)
        {
            if(Database.isEmpty())
            {
                return "Nothing to delete - database is empty";
            }

            sub = sub.trim();

            if(sub.isEmpty())
            {
                return "Subject cannot be empty";
            }

            double dur;

            try
            {
                dur = Double.parseDouble(durText.trim());
            }
            catch(NumberFormatException e)
            {
                return "Invalid duration. Log not deleted.";
            }

            if(dur <= 0)
            {
                return "Duration must be greater than 0. Log not deleted.";
            }

            StudyLog target = new StudyLog(LocalDate.now(), sub, dur, desc.trim());

            if(Database.remove(target))
            {
                return "Study log deleted successfully";
            }
            else
            {
                return "No matching log found to delete";
            }
        }

        // ---------------- 2 : Display ----------------
        public void DisplayLog()
        {
            System.out.println("-----------------------------------------");

            if(Database.isEmpty())
            {
                System.out.println("Nothing to display - database is empty");
                System.out.println("-----------------------------------------");
                return;
            }

            System.out.println("-Log Report of Marvellous Study Tracker- ");
            System.out.println("-----------------------------------------");

            for(StudyLog s : Database)
            {
                System.out.println(s);
            }

            System.out.println("-----------------------------------------");
        }

        // ---------------- 3 : Export ----------------
        public void ExportToCSV()
        {
            if(Database.isEmpty())
            {
                System.out.println("Nothing to export - database is empty");
                System.out.println("-----------------------------------------");
                return;
            }

            System.out.println("Enter the name that you want to create for CSV file");
            String FileName = sc.nextLine().trim();

            if(!FileName.endsWith(".csv"))
            {
                FileName = FileName + ".csv";
            }

            try(FileWriter fwobj = new FileWriter(FileName))
            {
                fwobj.write("Date,Subject,Duration,Description\n");

                for(StudyLog s : Database)
                {
                    fwobj.write(s.getDate() + "," +
                                s.getSubject() + "," +
                                s.getDuration() + "," +
                                s.getDescription() + "\n");
                }

                System.out.println("Data exported successfully to " + FileName);
                System.out.println("-----------------------------------------");
            }
            catch(IOException iobj)
            {
                System.out.println("Export failed : " + iobj.getMessage());
            }
        }

        // ---------------- 4 : Summary by date ----------------
        public void SummaryByDate()
        {
            System.out.println("-----------------------------------------");
            System.out.println(" Summary by Date from study tracker ");
            System.out.println("-----------------------------------------");

            TreeMap<LocalDate, Double> tobj = new TreeMap<LocalDate, Double>();

            for(StudyLog s : Database)
            {
                tobj.merge(s.getDate(), s.getDuration(), Double::sum);
            }

            if(tobj.isEmpty())
            {
                System.out.println("No data available");
            }

            for(Map.Entry<LocalDate, Double> e : tobj.entrySet())
            {
                System.out.println("Date : " + e.getKey() +
                                " Total Study Duration : " + e.getValue());
            }

            System.out.println("-----------------------------------------");
        }

        // ---------------- 5 : Summary by subject ----------------

        public void SummaryBySubject()
        {
            System.out.println("-----------------------------------------");
            System.out.println(" Summary by Subject from study tracker ");
            System.out.println("-----------------------------------------");

            TreeMap<String, Double> tobj = new TreeMap<String, Double>();

            for(StudyLog s : Database)
            {
                tobj.merge(s.getSubject(), s.getDuration(), Double::sum);
            }

            if(tobj.isEmpty())
            {
                System.out.println("No data available");
            }

            for(Map.Entry<String, Double> e : tobj.entrySet())
            {
                System.out.println("Subject : " + e.getKey() +
                                " Total Study Duration : " + e.getValue());
            }

            System.out.println("-----------------------------------------");
        }

        // ---------------- 6 : Load ----------------
        public void LoadFromCSV()
        {
            System.out.println("Enter the name of the CSV file to load");
            String FileName = sc.nextLine().trim();

            if(!FileName.endsWith(".csv"))
            {
                FileName = FileName + ".csv";
            }

            int loaded = 0;
            int skipped = 0;

            try(BufferedReader br = new BufferedReader(new FileReader(FileName)))
            {
                String line = br.readLine();

                while((line = br.readLine()) != null)
                {
                    if(line.trim().isEmpty())
                    {
                        continue;
                    }

                    String[] parts = line.split(",", 4);

                    try
                    {
                        if(parts.length < 4)
                        {
                            throw new IllegalArgumentException("Missing columns");
                        }

                        LocalDate d = LocalDate.parse(parts[0].trim());
                        String sub = parts[1].trim();
                        double dur = Double.parseDouble(parts[2].trim());
                        String desc = parts[3].trim();

                        Database.add(new StudyLog(d, sub, dur, desc));
                        loaded++;
                    }
                    catch(Exception e)
                    {
                        // bad date, bad number, missing column
                        skipped++;
                    }
                }

                System.out.println("-----------------------------------------");
                System.out.println("Records loaded  : " + loaded);
                System.out.println("Records skipped : " + skipped);
                System.out.println("-----------------------------------------");
            }
            catch(FileNotFoundException fobj)
            {
                System.out.println("File not found : " + FileName);
            }
            catch(IOException iobj)
            {
                System.out.println("Load failed : " + iobj.getMessage());
            }
        }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  PART B : METHODS FOR THE GUI
//  Same logic as Part A, but they take parameters and
//  RETURN data/messages instead of using Scanner/println.
//
//  InsertLog(sub, dur, desc)  : Validates input, adds a new log, returns a status message.
//  DeleteLog()                : Reads details from the console and removes the matching log.
//  GetLogs()                  : Returns the list of all study logs.
//  GetSummaryByDate()         : Returns a sorted map of total study hours per date.
//  GetSummaryBySubject()      : Returns a sorted map of total study hours per subject.
//  ExportToCSV(FileName)      : Saves all logs to the given CSV file, returns a status message.
//  LoadFromCSV(FileName)      : Loads logs from the given CSV file, returns a loaded/skipped count.
//
// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        public String InsertLog(String sub, String durText, String desc)
        {
            // comma would break the CSV
            sub = sub.trim().replace(",", " ");

            if(sub.isEmpty())
            {
                return "Subject cannot be empty";
            }

            double dur;

            try
            {
                dur = Double.parseDouble(durText.trim());
            }
            catch(NumberFormatException e)
            {
                return "Invalid duration. Log not inserted.";
            }

            if(dur <= 0)
            {
                return "Duration must be greater than 0. Log not inserted.";
            }

            Database.add(new StudyLog(LocalDate.now(), sub, dur, desc.trim()));

            return "Study log gets inserted successfully";
        }

        public void DeleteLog()
        {
            System.out.println("-----------------------------------------");
            System.out.println("---- Enter the details to delete log ----");
            System.out.println("-----------------------------------------");

            if(Database.isEmpty())
            {
                System.out.println("Nothing to delete - database is empty");
                System.out.println("-----------------------------------------");
                return;
            }

            LocalDate lobj = LocalDate.now();
            System.out.println("We are entering the date as : " + lobj);

            System.out.println("Enter the name of subject to Delete like C/C++/Java etc");
            String sub = sc.nextLine();

            System.out.println("Enter the time period of your study (hours) : ");
            double dur;

            try
            {
                dur = sc.nextDouble();
            }
            catch(InputMismatchException e)
            {
                sc.nextLine();
                System.out.println("Invalid duration. Log not deleted.");
                return;
            }

            sc.nextLine();

            if(dur <= 0)
            {
                System.out.println("Duration must be greater than 0. Log not deleted.");
                return;
            }

            System.out.println("Please provide the description of your study : ");
            String desc = sc.nextLine();

            StudyLog target = new StudyLog(lobj, sub, dur, desc);

            if(Database.remove(target))
            {
                System.out.println("Study log gets deleted successfully");
            }
            else
            {
                System.out.println("No matching log found to delete");
            }

            System.out.println("-----------------------------------------");
        }

        public ArrayList<StudyLog> GetLogs()
        {
            return Database;
        }

        // Summary by date : returns the map
        public TreeMap<LocalDate, Double> GetSummaryByDate()
        {
            TreeMap<LocalDate, Double> tobj = new TreeMap<LocalDate, Double>();

            for(StudyLog s : Database)
            {
                tobj.merge(s.getDate(), s.getDuration(), Double::sum);
            }

            return tobj;
        }

        // Summary by subject : returns the map
        public TreeMap<String, Double> GetSummaryBySubject()
        {
            TreeMap<String, Double> tobj = new TreeMap<String, Double>();

            for(StudyLog s : Database)
            {
                tobj.merge(s.getSubject(), s.getDuration(), Double::sum);
            }

            return tobj;
        }

        public String ExportToCSV(String FileName)
        {
            if(Database.isEmpty())
            {
                return "Nothing to export - database is empty";
            }

            FileName = FileName.trim();

            if(!FileName.endsWith(".csv"))
            {
                FileName = FileName + ".csv";
            }

            try(FileWriter fwobj = new FileWriter(FileName))
            {
                fwobj.write("Date,Subject,Duration,Description\n");

                for(StudyLog s : Database)
                {
                    fwobj.write(s.getDate() + "," +
                                s.getSubject() + "," +
                                s.getDuration() + "," +
                                s.getDescription() + "\n");
                }

                return "Data exported successfully to " + FileName;
            }
            catch(IOException iobj)
            {
                return "Export failed : " + iobj.getMessage();
            }
        }

        public String LoadFromCSV(String FileName)
        {
            FileName = FileName.trim();

            if(!FileName.endsWith(".csv"))
            {
                FileName = FileName + ".csv";
            }

            int loaded = 0;
            int skipped = 0;

            try(BufferedReader br = new BufferedReader(new FileReader(FileName)))
            {
                String line = br.readLine();

                while((line = br.readLine()) != null)
                {
                    if(line.trim().isEmpty())
                    {
                        continue;
                    }

                    String[] parts = line.split(",", 4);

                    try
                    {
                        if(parts.length < 4)
                        {
                            throw new IllegalArgumentException("Missing columns");
                        }

                        Database.add(new StudyLog(LocalDate.parse(parts[0].trim()),
                                                parts[1].trim(),
                                                Double.parseDouble(parts[2].trim()),
                                                parts[3].trim()));
                        loaded++;
                    }
                    catch(Exception e)
                    {
                        skipped++;
                    }
                }

                return "Records loaded : " + loaded + "\nRecords skipped : " + skipped;
            }
            catch(FileNotFoundException fobj)
            {
                return "File not found : " + FileName;
            }
            catch(IOException iobj)
            {
                return "Load failed : " + iobj.getMessage();
            }
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//  Author      :   Kshitij Nale
//  Date        :   01/11/2026
//
//  Class Name  :   GUI
//  Description :   Swing window of the application. It only draws the components and
//                  forwards button clicks to StudyTracker. No study logic lives here.
//
//  Objects :
//      tracker         : Reference to the StudyTracker that performs all the operations.
//      txtSubject      : Text box to enter the subject.
//      txtDuration     : Text box to enter the study duration in hours.
//      txtDescription  : Text box to enter the description of the study.
//      output          : Read-only area that displays logs, summaries and status messages.
//
//  Functions :
//      GUI(tracker)    : Constructor that builds the window, places the components
//                        and connects each button to its action.
//      Insert()        : Sends the entered values to the tracker to add a log, then clears the text boxes.
//      Delete()        : Sends the entered values to the tracker to delete a log, then clears the text boxes.
//      View()          : Shows all study logs in the output area.
//      ByDate()        : Shows the total study hours per date in the output area.
//      BySubject()     : Shows the total study hours per subject in the output area.
//      Ask(msg)        : Shows a popup asking for a file name and returns what the user typed.
//
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ---------------- GUI (only buttons -> tracker calls) ----------------
    // No study logic lives here.

    class GUI extends JFrame
    {
        private StudyTracker tracker;

        private JTextField txtSubject = new JTextField(10);
        private JTextField txtDuration = new JTextField(5);
        private JTextField txtDescription = new JTextField(18);
        private JTextArea output = new JTextArea();

        public GUI(StudyTracker tracker)
        {
            super("Study Tracker");
            this.tracker = tracker;

            // ----- layout (placing components) -----
            JPanel top = new JPanel();

            top.add(new JLabel("Subject:"));
            top.add(txtSubject);

            top.add(new JLabel("Hours:"));
            top.add(txtDuration);

            top.add(new JLabel("Description:"));
            top.add(txtDescription);

            JButton btnInsert = new JButton("Insert");
            JButton btnView = new JButton("View all");
            JButton btnByDate = new JButton("Summary by date");
            JButton btnBySub = new JButton("Summary by subject");
            JButton btnExport = new JButton("Export CSV");
            JButton btnLoad = new JButton("Load CSV");
            JButton btnDelete = new JButton("Delete Log");
            JButton btnExit = new JButton("Exit");

            JPanel bottom = new JPanel();

            bottom.add(btnInsert);
            bottom.add(btnView);
            bottom.add(btnByDate);
            bottom.add(btnBySub);
            bottom.add(btnExport);
            bottom.add(btnLoad);
            bottom.add(btnDelete);
            bottom.add(btnExit);

            output.setEditable(false);
            output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));

            add(top, BorderLayout.NORTH);
            add(new JScrollPane(output), BorderLayout.CENTER);
            add(bottom, BorderLayout.SOUTH);

            // ----- button  ->  tracker method -----
            btnInsert.addActionListener(e -> Insert());
            btnDelete.addActionListener(e -> Delete());
            btnView.addActionListener(e -> View());
            btnByDate.addActionListener(e -> ByDate());
            btnBySub.addActionListener(e -> BySubject());

            btnExport.addActionListener(e ->
            {
                output.setText(tracker.ExportToCSV(Ask("CSV file name to create")));
            });

            btnLoad.addActionListener(e ->
            {
                output.setText(tracker.LoadFromCSV(Ask("CSV file name to load")));
            });

            btnExit.addActionListener(e -> System.exit(0));

            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(900, 450);
            setLocationRelativeTo(null);
        }

        // ----- one small method per button -----
        private void Insert()
        {
            output.setText(tracker.InsertLog(txtSubject.getText(),
                                            txtDuration.getText(),
                                            txtDescription.getText()));

            txtSubject.setText("");
            txtDuration.setText("");
            txtDescription.setText("");
        }

        private void Delete()
        {
            output.setText(tracker.DeleteLog(txtSubject.getText(),
                                            txtDuration.getText(),
                                            txtDescription.getText()));

            txtSubject.setText("");
            txtDuration.setText("");
            txtDescription.setText("");
        }

        private void View()
        {
            StringBuilder sb = new StringBuilder();

            for(StudyLog s : tracker.GetLogs())
            {
                sb.append(s).append("\n");
            }

            output.setText(sb.length() == 0
                        ? "Nothing to display - database is empty"
                        : sb.toString());
        }

        private void ByDate()
        {
            StringBuilder sb = new StringBuilder();

            for(Map.Entry<LocalDate, Double> e : tracker.GetSummaryByDate().entrySet())
            {
                sb.append("Date : ").append(e.getKey())
                .append("  Total Study Duration : ").append(e.getValue())
                .append("\n");
            }

            output.setText(sb.length() == 0
                        ? "No data available"
                        : sb.toString());
        }

        private void BySubject()
        {
            StringBuilder sb = new StringBuilder();

            for(Map.Entry<String, Double> e : tracker.GetSummaryBySubject().entrySet())
            {
                sb.append("Subject : ").append(e.getKey())
                .append("  Total Study Duration : ").append(e.getValue())
                .append("\n");
            }

            output.setText(sb.length() == 0
                        ? "No data available"
                        : sb.toString());
        }

        // popup to ask for a file name
        private String Ask(String msg)
        {
            String name = JOptionPane.showInputDialog(this, msg);

            return (name == null) ? "" : name;
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//  Class Name  :   program868Final
//  Description :   Entry point of the application. It creates the shared Scanner and
//                  StudyTracker, then starts the console menu if "console" is passed
//                  as an argument, otherwise it starts the GUI.
//
//  Objects :
//      sobj            : Scanner shared by the console menu and the tracker for keyboard input.
//      stobj           : StudyTracker object that stores and manages all the study logs.
//      iChoice         : Menu option selected by the user in the console menu.
//
//  Functions :
//      main(A[])       : Starts the application in console mode or GUI mode based on the argument.
//      ConsoleMenu()   : Displays the menu in a loop and calls the matching tracker method until Exit.
//
//  Author      :   <Your Name>
//  Date        :   09/11/2026
//
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    class program868Final
    {
        public static void main(String A[])
        {
            Scanner sobj = new Scanner(System.in);
            StudyTracker stobj = new StudyTracker(sobj);

            if(A.length > 0 && A[0].equals("console"))
            {
                ConsoleMenu(stobj, sobj);
            }
            else
            {
                SwingUtilities.invokeLater(() -> new GUI(stobj).setVisible(true));
            }
        }

        static void ConsoleMenu(StudyTracker stobj, Scanner sobj)
        {
            int iChoice = 0;

            System.out.println("-----------------------------------------");
            System.out.println("-- Welcome to Marvellous Study Tracker --");
            System.out.println("-----------------------------------------");

            do
            {
                System.out.println("-----------------------------------------");
                System.out.println("Please select appropriate option : ");
                System.out.println("-----------------------------------------");

                System.out.println("1 : Insert new study log");
                System.out.println("2 : View all study logs");
                System.out.println("3 : Export study log to CSV");
                System.out.println("4 : Summary of study log by date");
                System.out.println("5 : Summary of study log by subject");
                System.out.println("6 : Load study logs from CSV");
                System.out.println("7 : Delete log");
                System.out.println("8 : Exit the application");

                System.out.println("-----------------------------------------");

                if(!sobj.hasNextInt())
                {
                    sobj.nextLine();

                    System.out.println("Please enter a number");
                    iChoice = 0;

                    continue;
                }

                iChoice = sobj.nextInt();

                sobj.nextLine();

                switch(iChoice)
                {
                    case 1:
                        stobj.InsertLog();
                        break;

                    case 2:
                        stobj.DisplayLog();
                        break;

                    case 3:
                        stobj.ExportToCSV();
                        break;

                    case 4:
                        stobj.SummaryByDate();
                        break;

                    case 5:
                        stobj.SummaryBySubject();
                        break;

                    case 6:
                        stobj.LoadFromCSV();
                        break;
                    
                    case 7 :
                        stobj.DeleteLog();
                        break;

                    case 8:
                        break;

                    default:
                        System.out.println("Please enter valid option");
                        break;
                }

            }while(iChoice != 8);

            System.out.println("-----------------------------------------");
            System.out.println("----- Thank for using Study Tracker -----");
            System.out.println("-----------------------------------------");
        }
    }