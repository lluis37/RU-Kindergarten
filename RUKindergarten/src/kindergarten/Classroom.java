package kindergarten;
/**
 * This class represents a Classroom, with:
 * - an SNode instance variable for students in line,
 * - an SNode instance variable for musical chairs, pointing to the last student in the list,
 * - a boolean array for seating availability (eg. can a student sit in a given seat), and
 * - a Student array parallel to seatingAvailability to show students filed into seats 
 * --- (more formally, seatingAvailability[i][j] also refers to the same seat in studentsSitting[i][j])
 * 
 * @author Ethan Chou
 * @author Kal Pandit
 * @author Maksims Kurjanovics Kravcenko
 */
public class Classroom {
    private SNode studentsInLine;             // when students are in line: references the FIRST student in the LL
    private SNode musicalChairs;              // when students are in musical chairs: references the LAST student in the CLL
    private boolean[][] seatingAvailability;  // represents the classroom seats that are available to students
    private Student[][] studentsSitting;      // when students are sitting in the classroom: contains the students

    /**
     * Constructor for classrooms. Do not edit.
     * @param l passes in students in line
     * @param m passes in musical chairs
     * @param a passes in availability
     * @param s passes in students sitting
     */
    public Classroom ( SNode l, SNode m, boolean[][] a, Student[][] s ) {
		studentsInLine      = l;
        musicalChairs       = m;
		seatingAvailability = a;
        studentsSitting     = s;
	}
    /**
     * Default constructor starts an empty classroom. Do not edit.
     */
    public Classroom() {
        this(null, null, null, null);
    }

    /**
     * This method simulates students coming into the classroom and standing in line.
     * 
     * Reads students from input file and inserts these students in alphabetical 
     * order to studentsInLine singly linked list.
     * 
     * Input file has:
     * 1) one line containing an integer representing the number of students in the file, say x
     * 2) x lines containing one student per line. Each line has the following student 
     * information separated by spaces: FirstName LastName Height
     * 
     * @param filename the student information input file
     */
    public void makeClassroom ( String filename ) {

        // WRITE YOUR CODE HERE
        StdIn.setFile(filename);
        int numberOfStudents = StdIn.readInt();

        // for-loop will populate studentsInLine with a new node for each student
        for (int i = 1; i <= numberOfStudents; i++) {
            String firstName = StdIn.readString();
            String lastName = StdIn.readString();
            int height = StdIn.readInt();

            Student s = new Student(firstName, lastName, height);
            sortAlphabeticalInLine(s);
            printStudentsInLine();
        }

        /*
        SNode current = studentsInLine; // current is the current node who's student is being compared against every other student in studentsInLine
        SNode ptr = studentsInLine.getNext(); // ptr traverses studentsInLine
        // nested while-loop used to perform selection sort on the linked list
        while (current != null) {
            SNode swap = null; // swap keeps track of the node who's student belongs in current node according to alphabetical order; used to swap students
            Student alphabeticalOrder = current.getStudent(); // keeps track of the student in the most alphabetical order (like min on a selection sort for an int[] of increasing order)
            Student currentStudent = current.getStudent(); // keeps the student information of current node that is later going to be swapped
            
            while (ptr != null) {
                Student ptrStudent = ptr.getStudent();
                int cmp = alphabeticalOrder.compareNameTo(ptrStudent);

                if (cmp > 0) {
                    // cmp > 0 means that ptrStudent comes before alphabeticalOrder (or ptrStudent is in the most alphabetical order); Therefore, make alphabetical order = ptr.student;
                    alphabeticalOrder = ptrStudent;
                    swap = ptr; // keep track of the SNode that we currently want to swap students with
                }
                ptr = ptr.getNext();
            }

            if (swap != null) {
                current.setStudent(alphabeticalOrder); // set the student of current to be alphabeticalOrder - This will put the student in the current most alphabetical order into where it belongs
                swap.setStudent(currentStudent); // set the student of the SNode which we found to contain the current most alphabetical order student to be currentStudent (perform a swap of students)
            }

            current = current.getNext(); // update current to the next node in studentInLine
            if (current != null) { // make sure that we do not get any out of bounds error for trying to look beyond the linked list (studentsInLine)
                ptr = current.getNext(); // update ptr to point to the node right after current
            }

        }
        */
        // studentsInLine should be in alphabetical order after the previous nested while-loop

    }

    /**
     * 
     * This method creates and initializes the seatingAvailability (2D array) of 
     * available seats inside the classroom. Imagine that unavailable seats are broken and cannot be used.
     * 
     * Reads seating chart input file with the format:
     * An integer representing the number of rows in the classroom, say r
     * An integer representing the number of columns in the classroom, say c
     * Number of r lines, each containing c true or false values (true denotes an available seat)
     *  
     * This method also creates the studentsSitting array with the same number of
     * rows and columns as the seatingAvailability array
     * 
     * This method does not seat students on the seats.
     * 
     * @param seatingChart the seating chart input file
     */
    public void setupSeats(String seatingChart) {

	// WRITE YOUR CODE HERE
        StdIn.setFile(seatingChart);
        int numberRows = StdIn.readInt();
        int numberCols = StdIn.readInt();

        seatingAvailability = new boolean[numberRows][numberCols];
        studentsSitting = new Student[numberRows][numberCols];

        // This nested for-loop will traverse the boolean[][] array seatingAvailability and populate
        // it with the values from the read file seatingChart
        for (int r = 0; r < numberRows; r++) {
            for (int c = 0; c < numberCols; c++) {
                seatingAvailability[r][c] = StdIn.readBoolean();
            }
        }

    }

    /**
     * 
     * This method simulates students taking their seats in the classroom.
     * 
     * 1. seats any remaining students from the musicalChairs starting from the front of the list
     * 2. starting from the front of the studentsInLine singly linked list
     * 3. removes one student at a time from the list and inserts them into studentsSitting according to
     *    seatingAvailability
     * 
     * studentsInLine will then be empty
     */
    public void seatStudents () {

	// WRITE YOUR CODE HERE
        // This nested for-loop will traverse the boolean[][] array seatingAvailability by going into a row
        // and going through the columns of that row, before then going into the next row and repeating
        for (int r = 0; r < seatingAvailability.length; r++) {
            for (int c = 0; c < seatingAvailability[r].length; c++) {
                // If there is an available seat at [r][c] and musicalChairs has a student in it,
                // then get and remove the student from musicalChairs, seat the student at [r][c], and update
                // seatingAvailability[r][c] = false since the seat now has a student and is no longer available
                if (seatingAvailability[r][c] == true && musicalChairs != null) {
                    Student fromChairs = musicalChairs.getStudent();
                    musicalChairs = null;
                    studentsSitting[r][c] = fromChairs;
                    seatingAvailability[r][c] = false;

                    // Otherwise, if there is an available seat at [r][c] and there is a student in studentsInLine,
                    // then get and remove the student from the front of studentsInLine, seat the student at [r][c], and
                    // update seatingAvailability[r][c] = false since the seat now has a student and is no longer available 
                } else if (seatingAvailability[r][c] == true && studentsInLine != null) {
                    Student fromLine = removeFromFrontOfLine();
                    studentsSitting[r][c] = fromLine;
                    seatingAvailability[r][c] = false;
                }
            }
        }
	
    }

    /**
     * Traverses studentsSitting row-wise (starting at row 0) removing a seated
     * student and adding that student to the end of the musicalChairs list.
     * 
     * row-wise: starts at index [0][0] traverses the entire first row and then moves
     * into second row.
     */
    public void insertMusicalChairs () {
        
        // WRITE YOUR CODE HERE
        // This nested for-loop traverses the Student[][] array studentsSitting by going into a row
        // and going through the columns of that row, before then going into the next row and repeating 
        for (int r = 0; r < studentsSitting.length; r++) {
            for (int c = 0; c < studentsSitting[r].length; c++) {
                // Check to see if there is a student sitting at seat [r][c]
                // If there is, add that student to the end of musicalChairs, remove the student from studentsSitting
                // by making studentsSitting[r][c] = null, and update seatingAvailability[r][c] = true since the seat is now available
                if (studentsSitting[r][c] != null) {
                    Student s = studentsSitting[r][c];
                    addToEndOfChairs(s);
                    studentsSitting[r][c] = null;
                    seatingAvailability[r][c] = true;
                }
            }
        }

     }

    /**
     * 
     * This method repeatedly removes students from the musicalChairs until there is only one
     * student (the winner).
     * 
     * Choose a student to be elimnated from the musicalChairs using StdRandom.uniform(int b),
     * where b is the number of students in the musicalChairs. 0 is the first student in the 
     * list, b-1 is the last.
     * 
     * Removes eliminated student from the list and inserts students back in studentsInLine 
     * in ascending height order (shortest to tallest).
     * 
     * The last line of this method calls the seatStudents() method so that students can be seated.
     */
    public void playMusicalChairs() {

        // WRITE YOUR CODE HERE
        int size = countMusicalChairs(); // number of students in musicalChairs

        while (size != 1) {
            int n = StdRandom.uniform(size);
            SNode eliminated = musicalChairs;

            for (int i = 0; i <= n; i++) {
                eliminated = eliminated.getNext();
            }

            Student eliminatedStudent = eliminated.getStudent();
            removeFromChairs(eliminated);
            sortHeightInLine(eliminatedStudent);
            size--;
        }

        seatStudents();

        // The following code performs a selection sort on studentsInLine after all of the students have been placed in the list.
        // The students are placed in height order.
        /*
        SNode current = studentsInLine; // current is the current node who's student is being compared against every other student in studentsInLine
        SNode ptr = studentsInLine.getNext(); // ptr traverses studentsInLine
        // nested while-loop used to perform selection sort on the linked list
        while (current != null) {
            SNode swap = null; // swap keeps track of the node who's student belongs in current node according to alphabetical order; used to swap students
            Student shortestStudent = current.getStudent();
            int shortest = shortestStudent.getHeight(); // keeps track of the student in the most alphabetical order (like min on a selection sort for an int[] of increasing order)
            Student currentStudent = current.getStudent(); // keeps the student information of current node that is later going to be swapped
            
            while (ptr != null) {
                Student ptrStudent = ptr.getStudent();
                int ptrStudentHeight = ptrStudent.getHeight();

                // shortest > ptrStudentHeight means that ptr.getStudent.getHeight() is the shortest; Therefore, make shortest = ptrStudentHeight;
                if (shortest > ptrStudentHeight) {
                    shortest = ptrStudentHeight;
                    shortestStudent = ptrStudent;
                    swap = ptr; // keep track of the SNode that we currently want to swap students with
                }

                ptr = ptr.getNext();
            }

            if (swap != null) {
                current.setStudent(shortestStudent); // set the student of current to be alphabeticalOrder - This will put the student in the current most alphabetical order into where it belongs
                swap.setStudent(currentStudent); // set the student of the SNode which we found to contain the current most alphabetical order student to be currentStudent (perform a swap of students)
            }

            current = current.getNext(); // update current to the next node in studentInLine
            if (current != null) { // make sure that we do not get any out of bounds error for trying to look beyond the linked list (studentsInLine)
                ptr = current.getNext(); // update ptr to point to the node right after current
            }

        }
        */

    } 

    /**
     * Insert a student to wherever the students are at (ie. whatever activity is not empty)
     * Note: adds to the end of either linked list or the next available empty seat
     * @param firstName the first name
     * @param lastName the last name
     * @param height the height of the student
     */
    public void addLateStudent ( String firstName, String lastName, int height ) {
        
        // WRITE YOUR CODE HERE
        Student s = new Student(firstName, lastName, height);

        if (studentsInLine != null) {
            addToEndOfLine(s);
        } else if (musicalChairs != null) {
            addToEndOfChairs(s);
        } else {
            int breakCheck = 0;
            for (int r = 0; r < seatingAvailability.length; r++) {
                for (int c = 0; c < seatingAvailability[r].length; c++) {
                    if (seatingAvailability[r][c] == true) {
                        studentsSitting[r][c] = s;
                        seatingAvailability[r][c] = false;
                        breakCheck = 1;
                        break;
                    }
                }
                
                if (breakCheck == 1) {
                    break;
                }
            }
        }
        
    }

    /**
     * A student decides to leave early
     * This method deletes an early-leaving student from wherever the students 
     * are at (ie. whatever activity is not empty)
     * 
     * Assume the student's name is unique
     * 
     * @param firstName the student's first name
     * @param lastName the student's last name
     */
    public void deleteLeavingStudent ( String firstName, String lastName ) {

        // WRITE YOUR CODE HERE
        firstName = new String(firstName);
        firstName = firstName.toUpperCase();
        lastName = new String(lastName);
        lastName = lastName.toUpperCase();
        Student s = new Student (firstName, lastName, 30);

        if (studentsInLine != null) {
            removeFromLine(firstName, lastName);
        } else if (musicalChairs != null) {
            removeFromChairs(firstName, lastName);
        } else {
            int breakCheck = 0;
            for (int r = 0; r < studentsSitting.length; r++) {
                for (int c = 0; c < studentsSitting[r].length; c++) {
                    Student sittingStudent = studentsSitting[r][c];

                    if (sittingStudent != null) {
                        String sittingStudentFN = sittingStudent.getFirstName();
                        String sittingStudentLN = sittingStudent.getLastName();


                        Student tempStudent = new Student(sittingStudentFN, sittingStudentLN, 30);
                        String tempStudentFN;
                        String tempStudentLN;

                        tempStudentFN = new String(sittingStudentFN);
                        tempStudentFN = tempStudentFN.toUpperCase();
                        tempStudentLN = new String(sittingStudentLN);
                        tempStudentLN = tempStudentLN.toUpperCase();

                        tempStudent.setFirstName(tempStudentFN);
                        tempStudent.setLastName(tempStudentLN);
                        int cmp = tempStudent.compareNameTo(s);

                        if (cmp == 0) {
                            studentsSitting[r][c] = null;
                            seatingAvailability[r][c] = true;
                            breakCheck = 1;
                            break;
                        }
                    }
                }

                if (breakCheck == 1) {
                    break;
                }
            }
        }

    }

    /**
     * Used by driver to display students in line
     * DO NOT edit.
     */
    public void printStudentsInLine () {

        //Print studentsInLine
        StdOut.println ( "Students in Line:" );
        if ( studentsInLine == null ) { StdOut.println("EMPTY"); }

        for ( SNode ptr = studentsInLine; ptr != null; ptr = ptr.getNext() ) {
            StdOut.print ( ptr.getStudent().print() );
            if ( ptr.getNext() != null ) { StdOut.print ( " -> " ); }
        }
        StdOut.println();
        StdOut.println();
    }

    /**
     * Prints the seated students; can use this method to debug.
     * DO NOT edit.
     */
    public void printSeatedStudents () {

        StdOut.println("Sitting Students:");

        if ( studentsSitting != null ) {
        
            for ( int i = 0; i < studentsSitting.length; i++ ) {
                for ( int j = 0; j < studentsSitting[i].length; j++ ) {

                    String stringToPrint = "";
                    if ( studentsSitting[i][j] == null ) {

                        if (seatingAvailability[i][j] == false) {stringToPrint = "X";}
                        else { stringToPrint = "EMPTY"; }

                    } else { stringToPrint = studentsSitting[i][j].print();}

                    StdOut.print ( stringToPrint );
                    
                    for ( int o = 0; o < (10 - stringToPrint.length()); o++ ) {
                        StdOut.print (" ");
                    }
                }
                StdOut.println();
            }
        } else {
            StdOut.println("EMPTY");
        }
        StdOut.println();
    }

    /**
     * Prints the musical chairs; can use this method to debug.
     * DO NOT edit.
     */
    public void printMusicalChairs () {
        StdOut.println ( "Students in Musical Chairs:" );

        if ( musicalChairs == null ) {
            StdOut.println("EMPTY");
            StdOut.println();
            return;
        }
        SNode ptr;
        for ( ptr = musicalChairs.getNext(); ptr != musicalChairs; ptr = ptr.getNext() ) {
            StdOut.print(ptr.getStudent().print() + " -> ");
        }
        if ( ptr == musicalChairs) {
            StdOut.print(musicalChairs.getStudent().print() + " - POINTS TO FRONT");
        }
        StdOut.println();
    }

    /**
     * Prints the state of the classroom; can use this method to debug.
     * DO NOT edit.
     */
    public void printClassroom() {
        printStudentsInLine();
        printSeatedStudents();
        printMusicalChairs();
    }

    /**
     * Used to get and set objects.
     * DO NOT edit.
     */

    public SNode getStudentsInLine() { return studentsInLine; }
    public void setStudentsInLine(SNode l) { studentsInLine = l; }

    public SNode getMusicalChairs() { return musicalChairs; }
    public void setMusicalChairs(SNode m) { musicalChairs = m; }

    public boolean[][] getSeatingAvailability() { return seatingAvailability; }
    public void setSeatingAvailability(boolean[][] a) { seatingAvailability = a; }

    public Student[][] getStudentsSitting() { return studentsSitting; }
    public void setStudentsSitting(Student[][] s) { studentsSitting = s; }

    private void addToEndOfLine(Student s) {
        if (studentsInLine == null) {
            studentsInLine = new SNode(s, null);
            return;
        }

        SNode addedToEnd = new SNode(s, null);
        SNode oldLast = studentsInLine;

        while (oldLast.getNext() != null) {
            oldLast = oldLast.getNext();
        }
        oldLast.setNext(addedToEnd);
    }

    private void sortAlphabeticalInLine(Student s) {
        if (studentsInLine == null) {
            studentsInLine = new SNode(s, null);
            return;
        }

        int cmp = studentsInLine.getStudent().compareNameTo(s);

        // Check if we want to add student at the front of studentsInLine
        if (cmp > 0) {
            SNode newStudentNode = new SNode(s, studentsInLine);
            studentsInLine = newStudentNode;
            return;
        }

        SNode prev = null;
        SNode ptr = studentsInLine;
        Student ptrStudent = ptr.getStudent();

        // If we do not want to add student to the front of studentsInLine, then
        // move ptr until we find the node that comes right before the new student node
        // (We say <= because we want the list to be sorted in alphabetical order, and when students have the same name,
        // we want the list to be sorted in order of insertion. s represents a new student that has been inserted into the 
        // list after the others, so we do <= to place them after the other students who come before them in
        // alphabetical order or have the same name.)
        while (cmp <= 0 && ptr != null) {
            prev = ptr;
            ptr = ptr.getNext();
            if (ptr != null) {
                ptrStudent = ptr.getStudent();
                cmp = ptrStudent.compareNameTo(s);
            }
        }

        // Place the student in the desired location
        SNode newStudentNode = new SNode(s, prev.getNext());
        prev.setNext(newStudentNode);
    }

    private void sortHeightInLine(Student s) {
        if (studentsInLine == null) {
            studentsInLine = new SNode(s, null);
            return;
        }

        int sHeight = s.getHeight();

        // Check if we want to add student at the front of studentsInLine
        if (studentsInLine.getStudent().getHeight() > sHeight) {
            SNode newStudentNode = new SNode(s, studentsInLine);
            studentsInLine = newStudentNode;
            return;
        }

        SNode prev = null;
        SNode ptr = studentsInLine;
        Student ptrStudent = ptr.getStudent();
        int ptrStudentHeight = ptrStudent.getHeight();

        // If we do not want to add student to the front of studentsInLine, then
        // move ptr until we find the node that comes right before the new student node
        // (We say <= because we want the list to be sorted from shortest to tallest, and when students have the same height,
        // we want the list to be sorted in order of insertion. s represents a new student that has been inserted into the 
        // list after the others, so we do <= to place them after the other students with the same or smaller height.)
        while (ptrStudentHeight <= sHeight && ptr != null) {
            prev = ptr;
            ptr = ptr.getNext();
            if (ptr != null) {
                ptrStudent = ptr.getStudent();
                ptrStudentHeight = ptrStudent.getHeight();
            }
        }

        // Place the student in the desired location
        SNode newStudentNode = new SNode(s, prev.getNext());
        prev.setNext(newStudentNode);
    }

    private Student removeFromFrontOfLine() {
        if (studentsInLine == null) {
            return null;
        }

        Student s = studentsInLine.getStudent();
        studentsInLine = studentsInLine.getNext();
        return s;
    }

    private void removeFromLine(String firstName, String lastName) {
        if (studentsInLine == null) {
            return;
        }

        Student s = new Student(firstName, lastName, 30);
        
        Student inLineStudent = studentsInLine.getStudent();
        String inLineStudentFN = inLineStudent.getFirstName();
        String inLineStudentLN = inLineStudent.getLastName();

        Student tempStudent = new Student(inLineStudentFN, inLineStudentLN, 30);
        String tempStudentFN;
        String tempStudentLN;

        tempStudentFN = new String(inLineStudentFN);
        tempStudentFN = tempStudentFN.toUpperCase();
        tempStudentLN = new String(inLineStudentLN);
        tempStudentLN = tempStudentLN.toUpperCase();

        tempStudent.setFirstName(tempStudentFN);
        tempStudent.setLastName(tempStudentLN);
        int cmp = tempStudent.compareNameTo(s);

        if (cmp == 0) {
            removeFromFrontOfLine();
            return;
        }

        SNode target = studentsInLine;
        Student targetStudent = target.getStudent();
        SNode prev = null;

        while (cmp != 0 && target.getNext() != null) {
            prev = target;
            target = target.getNext();
            targetStudent = target.getStudent();

            String targetStudentFN = targetStudent.getFirstName();
            String targetStudentLN = targetStudent.getLastName();

            tempStudentFN = new String(targetStudentFN);
            tempStudentFN = tempStudentFN.toUpperCase();
            tempStudentLN = new String(targetStudentLN);
            tempStudentLN = tempStudentLN.toUpperCase();

            tempStudent.setFirstName(tempStudentFN);
            tempStudent.setLastName(tempStudentLN);
            cmp = tempStudent.compareNameTo(s);
        }

        prev.setNext(target.getNext());

    } 

    private void addToEndOfChairs(Student s) {
        if (musicalChairs == null) {
            musicalChairs = new SNode(s, null);
            musicalChairs.setNext(musicalChairs);
            return;
        }

        SNode oldLast = musicalChairs;
        musicalChairs = new SNode(s, oldLast.getNext());
        oldLast.setNext(musicalChairs);
    }

    private int countMusicalChairs() {
        int size = 0;

        if (musicalChairs == null) {
            return size;
        }

        size = 1;
        SNode ptr = musicalChairs.getNext();
        while (ptr != musicalChairs) {
            size += 1;
            ptr = ptr.getNext();
        }
        return size;
    }

    private void removeFromChairs(SNode target) {
        if (musicalChairs.getNext() == musicalChairs) {
            musicalChairs = null;
            return;
        }
        
        SNode ptr = musicalChairs.getNext();
        SNode prev = musicalChairs;
        while (ptr != target) {
            prev = ptr;
            ptr = ptr.getNext();
        }

        prev.setNext(ptr.getNext());
        if (target == musicalChairs) {
            musicalChairs = prev;
        }

    }

    private void removeFromChairs(String firstName, String lastName) {
        if (musicalChairs == null) {
            return;
        }

        Student s = new Student(firstName, lastName, 30);
        Student musicalChairsStudent = musicalChairs.getStudent();

        String musicalChairsStudentFN = musicalChairsStudent.getFirstName();
        String musicalChairsStudentLN = musicalChairsStudent.getLastName();

        Student tempStudent = new Student(musicalChairsStudentFN, musicalChairsStudentLN, 30);
        String tempStudentFN;
        String tempStudentLN;

        tempStudentFN = new String(musicalChairsStudentFN);
        tempStudentFN = tempStudentFN.toUpperCase();
        tempStudentLN = new String(musicalChairsStudentLN);
        tempStudentLN = tempStudentLN.toUpperCase();

        tempStudent.setFirstName(tempStudentFN);
        tempStudent.setLastName(tempStudentLN);
        int cmp = tempStudent.compareNameTo(s);

        if (cmp == 0) {
            removeFromChairs(musicalChairs);
            return;
        }

        SNode target = musicalChairs.getNext();
        Student targetStudent = target.getStudent();

        while (cmp != 0) {
            target = target.getNext();
            targetStudent = target.getStudent();
            
            String targetStudentFN = targetStudent.getFirstName();
            String targetStudentLN = targetStudent.getLastName();

            tempStudentFN = new String(targetStudentFN);
            tempStudentFN = tempStudentFN.toUpperCase();
            tempStudentLN = new String(targetStudentLN);
            tempStudentLN = tempStudentLN.toUpperCase();

            tempStudent.setFirstName(tempStudentFN);
            tempStudent.setLastName(tempStudentLN);
            cmp = targetStudent.compareNameTo(s);
        }

        removeFromChairs(target);
    }

}
