//Basic Game Application
//Version 2
// Basic Object, Image, Movement
// Astronaut moves to the right.
// Threaded

//K. Chun 8/2018

//*******************************************************************************
//Import Section
//Add Java libraries needed for the game
//import java.awt.Canvas;

//Graphics Libraries
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;


//*******************************************************************************
// Class Definition Section

//step 1: add keylistner to implements
public class BasicGameApp implements Runnable, KeyListener {

   //Variable Definition Section
   //Declare the variables used in the program 
   //You can set their initial values too
   
   //Sets the width and height of the program window
	final int WIDTH = 1000;
	final int HEIGHT = 700;

	//Declare the variables needed for the graphics
	public JFrame frame;
	public Canvas canvas;
   public JPanel panel;
   
	public BufferStrategy bufferStrategy;
	public Image guyPic;
	public Image wheelsawpic;
	public Image wallpic;
	public Image finishpic;
	public Image backgroundpic;
	public Image gameoverpic;
	public Image victorypic;

   //Declare the objects used in the program
   //These are things that are made up of more than one variable type
	private mazemovement guy;
	private mazemovement wheelsaw;
	private mazemovement wall;
	private mazemovement finish;
	private mazemovement gameover;
	private mazemovement victory;

	// step 1: add astro array and say how big it is
	mazemovement [] wheelsawarray = new mazemovement[4];
	mazemovement [] wallarray = new mazemovement[9];

   // Main method definition
   // This is the code that runs first and automatically
	public static void main(String[] args) {
		BasicGameApp ex = new BasicGameApp();   //creates a new instance of the game
		new Thread(ex).start();                 //creates a threads & starts up the code in the run( ) method  
	}



   // Constructor Method
   // This has the same name as the class
   // This section is the setup portion of the program
   // Initialize your variables and construct your program objects here.
	public BasicGameApp() {
      
      setUpGraphics();
       
      //variable and objects
      //create (construct) the objects needed for the game and load up
		// creating all of the charecters and setting their movements
		guyPic = Toolkit.getDefaultToolkit().getImage("guy.png");
		wheelsawpic = Toolkit.getDefaultToolkit().getImage("wheelsaw.png");
		backgroundpic = Toolkit.getDefaultToolkit().getImage("Unknown.jpeg");
		wallpic = Toolkit.getDefaultToolkit().getImage("wall.jpeg");
		finishpic = Toolkit.getDefaultToolkit().getImage("finish.png");
		gameoverpic = Toolkit.getDefaultToolkit().getImage("Unknown.png");
		victorypic = Toolkit.getDefaultToolkit().getImage("victory.jpeg");
		guy = new mazemovement(20,580,60,40,0,0);
		finish = new mazemovement(900,50,60,60,0,0);

		gameover = new mazemovement(100000,100000, 1000,700,0,0);
		victory = new mazemovement(100000,100000,1000,700,0,0);
		// setting each individual wall to the right position
		for (int x = 0; wallarray.length >x; x++){
			wallarray[x] = new mazemovement(0,0,0,60,0,0);
		}
		wallarray[0].xpos = 0;// the preceding lines of code that seem similiar are setting each wall to its individual postion and ensuring the rec followed
		wallarray[0].ypos = 0;
		wallarray[0].width = 1000;
		wallarray[0].rec = new Rectangle(wallarray[0].xpos, wallarray[0].ypos, wallarray[0].width, wallarray[0].height);
		wallarray[1].xpos = 0;
		wallarray[1].ypos = 140;
		wallarray[1].width = 150;
		wallarray[1].rec = new Rectangle(wallarray[1].xpos, wallarray[1].ypos, wallarray[1].width, wallarray[1].height);
		wallarray[2].ypos=140;
		wallarray[2].width=700;
		wallarray[2].xpos = 350;
		wallarray[2].rec = new Rectangle(wallarray[2].xpos, wallarray[2].ypos, wallarray[2].width, wallarray[2].height);
		wallarray[3].ypos=300;
		wallarray[3].width=550;
		wallarray[3].xpos = 500;
		wallarray[3].rec = new Rectangle(wallarray[3].xpos, wallarray[3].ypos, wallarray[3].width, wallarray[3].height);
		wallarray[4].ypos = 300;
		wallarray[4].width=300;
		wallarray[4].xpos = 0;
		wallarray[4].rec = new Rectangle(wallarray[4].xpos, wallarray[4].ypos, wallarray[4].width, wallarray[4].height);
		wallarray[5].ypos=500;
		wallarray[5].width=200;
		wallarray[5].xpos = 800;
		wallarray[5].rec = new Rectangle(wallarray[5].xpos, wallarray[5].ypos, wallarray[5].width, wallarray[5].height);
		wallarray[6].ypos=500;
		wallarray[6].width=600;
		wallarray[6].xpos = 0;
		wallarray[6].rec = new Rectangle(wallarray[6].xpos, wallarray[6].ypos, wallarray[6].width, wallarray[6].height);
		wallarray[7].ypos=650;
		wallarray[7].width=1000;
		wallarray[7].xpos = 1;
		wallarray[7].height = 30; //different hieght so it doesn't instantly kill the player
		wallarray[7].rec = new Rectangle(wallarray[7].xpos, wallarray[7].ypos, wallarray[7].width, wallarray[7].height);
		wallarray[8].ypos=80;
		wallarray[8].width=70;
		wallarray[8].xpos = 0;
		wallarray[8].rec = new Rectangle(wallarray[8].xpos, wallarray[8].ypos, wallarray[8].width, wallarray[8].height);
		for(int x = 0;x<wheelsawarray.length; x++){
			int yps = x*200;
			int xps = x*100; // setting the wheelsaws the be seperate
			wheelsawarray[x] = new mazemovement(xps,yps,60,60,3,0);
			wheelsawarray[x].rec = new Rectangle(wheelsawarray[x].xpos, wheelsawarray[x].ypos, wheelsawarray[x].width, wallarray[8].height);

		}
		wheelsawarray[0].ypos = 80; // to fix the first wheel so it isn't stuck in a wall



	}// BasicGameApp()

   
//*******************************************************************************
//User Method Section
//
// put your code to do things here.

   // main thread
   // this is the code that plays the game after you set things up
	public void run() {

      //for the moment we will loop things forever.
		while (true) {

         moveThings();  //move all the game objects
         render();  // paint the graphics
         pause(20); // sleep for 10 ms
		}
	}


	public void moveThings()
	{
      //calls the move( ) code in the objects
		collision();
		guy.wrap(); // the movement for the guy. he can't cheat beacuse of the walls i've set
		for(int y = 0; y<wheelsawarray.length ; y++){
			wheelsawarray[y].bounce(); // here is the movement for the wheelsaw
		}

	}

	public void collision(){
		for (int x = 0;x<wheelsawarray.length; x++){
// collisions from guy to wheelsaws

		if(guy.rec.intersects(wheelsawarray[x].rec) && guy.crash == false && wheelsawarray[x].isAlive == true && guy.isAlive == true){
			System.out.println("splat");
			guy.isAlive = false;
			gameover.xpos = 0;
			gameover.ypos = 0; // center end screen
			guy.xpos = 20; // return guy to start position so it doesn't continue crashing with what it hit
			guy.ypos = 580;
			for(int y =0; y <wheelsawarray.length;y++){
				wheelsawarray[y].dx = 0; // ensuring not movement after collision
				wheelsawarray[y].dy = 0;
			}
			guy.crash = true;
		}
		if(!guy.rec.intersects(wheelsawarray[x].rec)){
			guy.crash = false;
		}}
		for (int x = 0;x<wallarray.length; x++){

// same thing as the wheelsaws but this instead is for the walls.
			if(guy.rec.intersects(wallarray[x].rec) && guy.crash == false && wallarray[x].isAlive == true && guy.isAlive == true){
				System.out.println("splat");
				guy.isAlive = false;
				gameover.xpos = 0;
				gameover.ypos = 0;
				guy.xpos = 20;
				guy.ypos = 580;
				guy.crash = true;
			}
			if(!guy.rec.intersects(wallarray[x].rec)){
				guy.crash = false;
			}}


// same thing as the wheelsaws but this instead is for the walls.
			if(guy.rec.intersects(finish.rec) && guy.crash == false && finish.isAlive == true && guy.isAlive == true){
				System.out.println("splat");
				guy.isAlive = false;
				victory.xpos = 0;
				victory.ypos = 0; // sets win screen center
				guy.crash = true;
			}
			if(!guy.rec.intersects(finish.rec)){
				guy.crash = false;
			}

	}

	
   //Pauses or sleeps the computer for the amount specified in milliseconds
   public void pause(int time ){
   		//sleep
			try {
				Thread.sleep(time);
			} catch (InterruptedException e) {
			}
   }

   //Graphics setup method
   private void setUpGraphics() {
      frame = new JFrame("Application Template");   //Create the program window or frame.  Names it.
   
      panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
      panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
      panel.setLayout(null);   //set the layout
   
      // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
      // and trap input events (Mouse and Keyboard events)
      canvas = new Canvas();  
      canvas.setBounds(0, 0, WIDTH, HEIGHT);
      canvas.setIgnoreRepaint(true);
   // step 2: add key listner to canvas as this
	   canvas.addKeyListener(this);
      panel.add(canvas);  // adds the canvas to the panel.
      // frame operations
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
      frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
      frame.setResizable(false);   //makes it so the frame cannot be resized
      frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!
      
      // sets up things so the screen displays images nicely.
      canvas.createBufferStrategy(2);
      bufferStrategy = canvas.getBufferStrategy();
      canvas.requestFocus();
      System.out.println("DONE graphic setup");
   
   }


	//paints things on the screen using bufferStrategy
	private void render() {
		Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
		g.clearRect(0, 0, WIDTH, HEIGHT);
		g.drawImage(backgroundpic,0,0, WIDTH, HEIGHT, null);
		g.drawImage(guyPic, guy.xpos, guy.ypos, guy.width, guy.height, null);
		for(int x =0;x<wallarray.length;x++){
			g.drawImage(wallpic, wallarray[x].xpos, wallarray[x].ypos, wallarray[x].width, wallarray[x].height, null);
		}
		g.drawImage(finishpic, finish.xpos, finish.ypos, finish.width, finish.height, null);
		for(int z=0; z<wheelsawarray.length;z++){ // drawing each wheelsaw
		if(guy.isAlive == true && wheelsawarray[z].isAlive == true) {
			g.drawImage(wheelsawpic, wheelsawarray[z].xpos, wheelsawarray[z].ypos, wheelsawarray[z].width, wheelsawarray[z].height, null);
		}
			g.drawImage(wheelsawpic, wheelsawarray[z].xpos, wheelsawarray[z].ypos, wheelsawarray[z].width, wheelsawarray[z].height, null);
		}
		for(int z=0; z<wallarray.length;z++){ // drawing each wall
			g.drawImage(wallpic, wallarray[z].xpos, wallarray[z].ypos, wallarray[z].width, wallarray[z].height, null);
		}
		g.drawImage(gameoverpic,gameover.xpos,gameover.ypos,gameover.width,gameover.height,null);
		g.drawImage(victorypic,victory.xpos,victory.ypos,victory.width,victory.height,null);
		g.dispose();
		bufferStrategy.show();
	}


	@Override
	public void keyTyped(KeyEvent e) {//dont touch

	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println("key pressed");
		System.out.println(e.getKeyChar());
		System.out.println(e.getKeyCode());

		if(e.getKeyCode() == 38){
			System.out.println("going up");
			guy.up = true;

		}
		if(e.getKeyCode() == 37){
			System.out.println("going left");
			guy.left = true;


		}
		if(e.getKeyCode() == 40){
			System.out.println("going down");
			guy.down = true;

		}
		if(e.getKeyCode() == 39){
			System.out.println("going right");
			guy.right = true;
//
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == 38){
			System.out.println("not going up");
			guy.up = false;

		}
		if(e.getKeyCode() == 37){
			System.out.println("not going left");
			guy.left = false;


		}
		if(e.getKeyCode() == 40){
			System.out.println("not going down");
			guy.down = false;

		}
		if(e.getKeyCode() == 39){
			System.out.println("not going right");
			guy.right = false;
//
		}
//

    }
	//
}