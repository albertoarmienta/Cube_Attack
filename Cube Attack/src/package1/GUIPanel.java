package package1;
import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

class GUIPanel extends Applet implements ActionListener{
    //public static final int WIDTH = 1000, HEIGHT = 825;
    
    public static int HEIGHT = getScreenWorkingHeight()*3/4,WIDTH = HEIGHT*6/5;
    public int previousW = WIDTH, previousH = HEIGHT;
    public static final double RATIO = WIDTH/HEIGHT;
    public int gameState = 0;
    public AudioHandler audio = new AudioHandler();
    public Board b1;
    public Board b2;
    public MidColumn b3;
    public JLabel banner;
    private JLabel menu;
    private JFrame game = buildFrame();
    public Clip currentSong;
    //ComboStreakDisplay.setLineWrap(true);
    public GUIPanel()
    {
        menu = new JLabel();
        menu.setBounds(0,0,WIDTH,HEIGHT);
        ImageIcon menuIcon = new ImageIcon(getClass().getResource("MENU.png"));
        Image scaledIcon = menuIcon.getImage().getScaledInstance(WIDTH, HEIGHT, java.awt.Image.SCALE_SMOOTH);
        menu.setIcon(new ImageIcon(scaledIcon));
        game.getContentPane().add(menu);
        setFocusable(true);
        game.addKeyListener(new TAdapter());
        game.setVisible(true);
        game.setLocationRelativeTo(null);
        currentSong = audio.playTitleSong();
        
    }
    //Get the 'working' height and width of users monitor
    public static int getScreenWorkingWidth() {
        return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
    }
    
    public static int getScreenWorkingHeight() {
        return java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
    }
    public JFrame buildFrame() {
        JFrame window = new JFrame("Cube Attack");
        window.setLayout(null);
        window.getContentPane().setPreferredSize(new Dimension( WIDTH, HEIGHT));
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        return window;
    }
    public void startGame1(){
        gameState = 1;
        game.remove(menu);
        b1 = new Board(false, this,1);
        b2 = new Board(true, this,2);
        b3 = new MidColumn(this,1);
        //SetupDisplay() sets the boards and banners accordingly
        SetupDisplay();
        
    }
    public void startGame2(){
        gameState = 2;
        game.remove(menu);
        b1 = new Board(false, this,1);
        b2 = new Board(false, this,2);
        b3 = new MidColumn(this,2);
        //SetupDisplay() sets the boards and banners accordingly
        SetupDisplay();
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    private class TAdapter extends KeyAdapter {
        
        public void keyReleased(KeyEvent e) {
            
        }
        
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            switch(key){
                case KeyEvent.VK_1:
                    if(gameState == 1){
                        //b1.moveUp();
                        //b2.moveUp();
                        
                    }
                    else if(gameState==0)
                    {
                        startGame1();
                        audio.playSelectSound();
                        audio.stopSong(currentSong);
                        currentSong = audio.playGameSong1();
                    }
                    break;
                case KeyEvent.VK_2:
                    if(gameState == 1){
                        //b1.moveUp();
                        //b2.moveUp();
                    }
                    else if(gameState==0)
                    {
                        startGame2();
                        audio.stopSong(currentSong);
                        currentSong = audio.playGameSong1();
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if(gameState == 1){
                        b1.swapTargets();
                        audio.playSwapSound();
                    }
                    else if (gameState == 2)
                        b1.swapTargets();
                    audio.playSwapSound();
                    break;
                case KeyEvent.VK_UP:
                    if(gameState == 1){
                        b1.levelCursor.moveUp();
                        audio.playMoveSound();
                    }
                    else if (gameState == 2){
                        b2.levelCursor.moveUp();
                        audio.playMoveSound();
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(gameState == 1){
                        b1.levelCursor.moveDown();
                        audio.playMoveSound();
                    }
                    else if (gameState == 2){
                        b2.levelCursor.moveDown();
                        audio.playMoveSound();
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if(gameState == 1){
                        b1.levelCursor.moveLeft();
                        audio.playMoveSound();
                    }
                    else if (gameState == 2){
                        b2.levelCursor.moveLeft();
                        audio.playMoveSound();
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(gameState == 1){
                        b1.levelCursor.moveRight();
                        audio.playMoveSound();
                    }
                    else if (gameState == 2){
                        b2.levelCursor.moveRight();
                        audio.playMoveSound();
                    }
                    break;
                case KeyEvent.VK_W:
                    if (gameState == 2){
                        b1.levelCursor.moveUp();
                        audio.playMoveSound();
                    }
                    break;
                case KeyEvent.VK_S:
                    if (gameState == 2){
                        b1.levelCursor.moveDown();
                        audio.playMoveSound();
                    }
                    break;
                case KeyEvent.VK_A:
                    if (gameState == 2){
                        b1.levelCursor.moveLeft();
                        audio.playMoveSound();
                    }
                    break;
                case KeyEvent.VK_D:
                    if (gameState == 2){
                        b1.levelCursor.moveRight();
                        audio.playMoveSound();
                    }
                    break;
                case KeyEvent.VK_ENTER:
                    if (gameState == 2){
                        b2.swapTargets();
                        audio.playSwapSound();
                    }
                    //if you are going against the AI, enter will move
                    //move blocks up
                    if(gameState == 1)
                    {
                        b1.moveUp();
                        b1.generateRow();
                    }
                    break;
                case KeyEvent.VK_0:
                    if(gameState==1)
                    {
                        b1.generateBricks(1);
                        //game.dispose();
                        //gameState = 0;
                    }
                    break;
                case KeyEvent.VK_EQUALS:
                    if(gameState==1)
                    {
                        b2.generateBricks(1);
                        //game.dispose();
                        //gameState = 0;
                    }
                    break;
                case KeyEvent.VK_9:
                    audio.stopSong(currentSong);
            }
            
        }
    }
    
    private void SetupDisplay()
    {
        b1.setBounds(0,0,b1.WIDTH-(b1.OFFSET),b1.HEIGHT);
        b2.setBounds(b2.WIDTH + b3.WIDTH-(b1.OFFSET),0,b2.WIDTH,b2.HEIGHT);
        b3.setBounds(b1.WIDTH-(b1.OFFSET), 0, b3.WIDTH, b1.HEIGHT);
        // If you want text to appear above banner, (..).add() the text before
        // the Banner/picture
        game.getContentPane().add(b1);
        game.getContentPane().add(b2);
        game.getContentPane().add(b3);
        game.repaint();
        
    }
    
    // This function is called within the board class, and it will pass a
    // reference to itself. Always put blocks on opposite board
    public void addBricks(Board whichBoard, int comboSize)
    {
        //b1 is left board
        if(whichBoard == b1)
        {
            b2.generateBricks(comboSize);
        }
        else if(whichBoard == b2)
        {
            b1.generateBricks(comboSize);
        }
    }

    // This function is called within the board class, and it will pass a
    // reference to itself. This tells the that the current board 
    // For now when you lose you are brought to the main menu
    public void endRound(Board whichBoard)
    {
        b1.stopThreads();
        b2.stopThreads();
        ImageIcon menuIcon = new ImageIcon(getClass().getResource("MENU.png"));
        Image scaledIcon = menuIcon.getImage().getScaledInstance(WIDTH, HEIGHT, java.awt.Image.SCALE_SMOOTH);
        menu.setIcon(new ImageIcon(scaledIcon));
        game.remove(b1);
        game.remove(b2);
        game.remove(b3);
        game.getContentPane().add(menu);
        gameState = 0;
        game.repaint();
        //this stops the timer in the MidColumn 
        b3.stopTimer();
    }
}
