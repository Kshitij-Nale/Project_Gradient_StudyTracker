# 📚 Gradient - StudyTracker

A lightweight Java application to log, review, and analyze your study sessions. It comes with **two interfaces in one program**: a Swing-based **GUI** (default) and a classic **console menu**. Study data can be exported to and loaded from CSV files.

Built with core Java only. No external libraries or build tools needed.

---

## ✨ Features

- ➕ **Insert** study logs (date, subject, duration in hours, description)
- 👀 **View** all logs
- 🗑️ **Delete** a matching log
- 📅 **Summary by date**: total hours studied per day
- 🏷️ **Summary by subject**: total hours studied per subject (C, C++, Java, etc.)
- 📤 **Export** logs to a CSV file
- 📥 **Load** logs from a CSV file (bad rows are skipped and counted)
- 🖥️ **Dual interface**: Swing GUI or console menu
- ✅ Input validation (empty subject, invalid or non-positive duration, missing files)

---

## 🛠️ Tech Stack

| Area | Details |
|------|---------|
| Language | Java 8 or higher |
| GUI | Swing (`javax.swing`, `java.awt`) |
| Date handling | `java.time.LocalDate` |
| Storage | In-memory `ArrayList` + CSV import/export |
| Summaries | `TreeMap` (automatically sorted) |

---

## 🚀 Getting Started

### Prerequisites

- JDK 8 or newer. Check with:

```bash
java -version
javac -version
```

### Clone the repository

```bash
git clone https://github.com/<your-username>/<your-repo-name>.git
cd <your-repo-name>
```

### Compile

```bash
javac program868Final.java
```

### Run

**GUI mode (default):**

```bash
java program868Final
```

**Console mode:**

```bash
java program868Final console
```

---

## 🖥️ Usage

### GUI mode

1. Fill in **Subject**, **Hours**, and **Description** at the top of the window.
2. Click a button at the bottom:

| Button | What it does |
|--------|--------------|
| Insert | Adds a new log with today's date |
| View all | Shows every log in the output area |
| Summary by date | Total hours per date |
| Summary by subject | Total hours per subject |
| Export CSV | Asks for a file name and saves all logs |
| Load CSV | Asks for a file name and loads logs from it |
| Delete Log | Removes the log matching the entered Subject, Hours, and Description |
| Exit | Closes the application |

### Console mode

```
-----------------------------------------
-- Welcome to Marvellous Study Tracker --
-----------------------------------------
1 : Insert new study log
2 : View all study logs
3 : Export study log to CSV
4 : Summary of study log by date
5 : Summary of study log by subject
6 : Load study logs from CSV
7 : Delete log
8 : Exit the application
-----------------------------------------
```

Type the option number and press Enter.

### Sample output

```
2026-11-09 | Java | 2.5 | Practiced collections and file handling
2026-11-09 | C++  | 1.0 | Revised pointers
```

```
Subject : C++   Total Study Duration : 1.0
Subject : Java  Total Study Duration : 2.5
```

---

## 📄 CSV Format

Exported files use this layout (with a header row):

```csv
Date,Subject,Duration,Description
2026-11-09,Java,2.5,Practiced collections and file handling
2026-11-09,C++,1.0,Revised pointers
```

- Dates use the ISO format `yyyy-MM-dd`.
- Duration is in hours (decimal allowed).
- Commas inside the **description** are supported when loading.
- A `.csv` extension is added automatically if you leave it out.
- Rows with a bad date, bad number, or missing columns are skipped and reported after loading.

---

## 🧱 Project Structure

The code is split into small classes, each with a single responsibility:

| Class | Responsibility |
|-------|----------------|
| `StudyLog` | Data model for one study entry (with `equals`/`hashCode` for deletion) |
| `StudyTracker` | All application logic: insert, delete, summaries, CSV export/load. Includes both console methods and GUI-friendly methods that return data or messages |
| `GUI` | Swing window that only draws components and forwards button clicks to `StudyTracker` |
| `program868Final` | Entry point. Chooses GUI or console mode based on the command-line argument |

---

## ⚠️ Known Limitations

- Data lives in memory. Use **Export CSV** before closing, or it will be lost.
- **Delete** matches on date, subject, duration, and description. Since the date is set to today's date, only logs created today can be deleted this way.
- Loading a CSV **appends** to existing logs, so loading the same file twice creates duplicates.
- In the GUI, commas in the subject are replaced with spaces to keep the CSV valid.

---

## 🔮 Future Improvements

- Automatic save/load on startup and exit
- Choose a custom date when inserting a log
- Delete by selecting a row in a table view
- Charts for weekly and monthly progress
- Edit existing logs
- Unit tests

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!

1. Fork the project
2. Create a feature branch (`git checkout -b feature/my-feature`)
3. Commit your changes (`git commit -m "Add my feature"`)
4. Push to the branch (`git push origin feature/my-feature`)
5. Open a Pull Request

---

## 📜 License

This project is licensed under the [MIT License](LICENSE). Add a `LICENSE` file to your repo, or change this section to match the license you choose.

---

## 👤 Author

**Your Name**
GitHub: [@your-username](https://github.com/your-username)

⭐ If you found this project useful, consider giving it a star!
