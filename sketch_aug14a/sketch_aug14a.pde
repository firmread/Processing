import maxlink.*;

// declaring variables
// out to max
// ** added for MaxLink
MaxLink link = new MaxLink(this, "bouncer"); 

//in if statement
link.output("boing"); // ** added for MaxLink
link.output("bing"); // ** added for MaxLink

// on max
// mxj jk.link bouncer 0 1



MaxLink link = new MaxLink(this,"color_sketch");

// in setup
  link.declareInlet("r");
  link.declareInlet("g");
  link.declareInlet("b");
  
//on max  
//mxj jk.link color_sketch 3 0
