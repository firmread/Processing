// Display 2010 calendar


import javax.swing.JOptionPane;

// Dimensions of window
int winwidth = 450;
int winheight = 385;


int daysinweek = 7;

// Number of rows in the grid
int numrows = 6;

// Margin at top of grid
int topmargin = 65;

// Margin around other sides of grid
int othermargin = 15;

// Width and height of a day in the grid
int gridw = (winwidth - 2 * othermargin) / daysinweek;
int gridh = (winheight - topmargin - othermargin) / numrows;

int gridx = othermargin;
int gridy = topmargin;
 
int month = 1;

// Start day of the current month
// Mon=0, Tue=1, Wed=2, etc
int startday = 4;

// Number of days in the current month
int numdays = daysinmonth (month);

PFont headingfont = createFont ("Georgia", 24);


PFont columnfont = createFont ("Georgia", 14);

PFont numberfont = createFont ("Georgia", 36);
void setup ()
{
 size (winwidth, winheight);
 textAlign (CENTER, CENTER);
 int x = 0;
 while (x < 365)
 {
   diaryEntry[x] = " ";
   x = x+1;

 }
}

void draw ()
{
 background (255);
 drawheading ();
 drawgrid ();  
}

void drawheading ()
{
 if (month == 1) displayheading ("January 2010");
 else if (month == 2) displayheading ("February 2010");
 else if (month == 3) displayheading ("March 2010");
 else if (month == 4) displayheading ("April 2010");
 else if (month == 5) displayheading ("May 2010");
 else if (month == 6) displayheading ("June 2010");
 else if (month == 7) displayheading ("July 2010");
 else if (month == 8) displayheading ("August 2010");
 else if (month == 9) displayheading ("September 2010");
 else if (month == 10) displayheading ("October 2010");
 else if (month == 11) displayheading ("November 2010");
 else if (month == 12) displayheading ("December 2010");  
}

// Display the given heading
void displayheading (String heading)
{
 textFont (headingfont);
 fill (0);
 text (heading, winwidth / 2, topmargin / 3);
}

// Draw the month grid
void drawgrid ()
{
 drawsquares ();
 drawcollabels ();
 drawnumbers ();
}

void drawsquares ()
{
 fill (200);
 for (int col = 0; col < daysinweek; col++) {
   for (int row = 0; row < numrows; row++) {
     rect (gridx + col * gridw, gridy + row * gridh, gridw, gridh);
   }
 }
}

void drawcollabels ()
{
 drawlabel ("Mon", 0);
 drawlabel ("Tue", 1);
 drawlabel ("Wed", 2);
 drawlabel ("Thu", 3);
 drawlabel ("Fri", 4);
 drawlabel ("Sat", 5);
 drawlabel ("Sun", 6);  
}

// Draw the given label at the top of the given column
void drawlabel (String label, int col)
{  
 fill (0);
 textFont (columnfont);
 text (label, gridx + gridw * col + gridw / 2, gridy - othermargin);
}

// Draw the numbers in the grid positions
void drawnumbers ()
{
 textFont (numberfont);
 fill (0);
 
 // Set the starting position based on the start day
 int numx = gridx + gridw * startday + gridw / 2;
 int numy = gridy + gridh / 3;
 
 // Add each day number to the grid
 int dayofweek = startday;
 for (int numday = 1; numday <= numdays; numday++) {
   
   // Actually draw the number
   String p = diaryEntry[startofmonth + numday];
   if (p.equals(" ") == false)  //error is here***********************************      
   {
     fill(255,0,0);
   }    
   else
   {
     fill(0,0,0);
   }
   text (numday, numx, numy);
   
   dayofweek++;
   if (dayofweek > 6) {
     dayofweek = 0;
     numy += gridh;
     numx = gridx + gridw / 2;
   } else {
     numx += gridw;
   }
 }

}

//   - n: move to next month
//   - p: move to previous month
void keyPressed ()
{
 if ((key == 'n') && (month != 12)) {
   month++;
   startday = (startday + numdays) % daysinweek;
   numdays = daysinmonth (month);
   startofmonth = Lookup2[month-1];
   println(startofmonth);
 } else if ((key == 'p') && (month != 1)) {
   month--;
   numdays = daysinmonth (month);
   println ((startday - numdays) % daysinweek);
   startday = (startday - numdays) % daysinweek;
   startofmonth = Lookup2[month-1];
   println(startofmonth);
   if (startday < 0) {
     startday += daysinweek;
   }
 }
}

int daysinmonth (int month)
{
 if ((month == 4) || (month == 6) || (month == 9) || (month == 11)) {
   return 30;
 } else if (month == 2) {
   return 28;
 } else {
   return 31;
 }
}

int startofmonth = 0;
int[] Lookup2 = {0,31,59,90,151,181,212,243,274,305,335};
String myText;
String[] diaryEntry = new String [365];
int xlocation = 0;
int ylocation = 0;
int numbercount = 0;
int store1 = 0;
int findDay(int xord, int yord)
{
int[][] Lookup = {
 {1, 2, 3, 4, 5, 6, 7},
 {8, 9, 10, 11, 12, 13, 14},
 {15, 16, 17, 18, 19, 20, 21},
 {22, 23, 24, 25, 26, 27, 28},
 {29, 30, 31, 32, 33, 34, 35},
 {36, 37, 38, 39, 40, 41, 42}
};  
int gridPos = -1;
 if (xord >= othermargin && xord <= winwidth - othermargin
     && yord > topmargin && yord <= winheight -othermargin)
 {
 xlocation = int((xord-15)/60);
 ylocation = int((yord-65)/50);
 gridPos = Lookup[ylocation][xlocation] - startday;
 if (gridPos < 1 || gridPos > 31)
 {
   gridPos = -1;
 }
 }
return(gridPos);
}


void mousePressed()
{
int x = 0;  
x = findDay(mouseX,mouseY);  
if (x > 0)
{
noLoop();
String info = (String) JOptionPane.showInputDialog("Enter:", diaryEntry[startofmonth + x]);
if (info != null) // null if Cancel is chosen
{
  diaryEntry[startofmonth + x] = info;
} 

loop();

}
 
}

