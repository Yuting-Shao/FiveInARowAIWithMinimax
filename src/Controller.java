import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * The Controller class implements the interface IController, which controls the process of the Five in A Row game.
 * It implements the java.awt.event.ActionListener to listen the button event in the panel and the actionPerformed
 * method is provided to perform different actions when corresponding button is clicked. It implements the
 * java.awt.event.MouseListener to listen the clicking of mouse on the board and the mouseClicked method is provided
 * to handle the event of the mouse clicking on the board.
 */
public class Controller implements ActionListener, MouseListener, IController{
    private Model model;
    private View view;

    /**
     * Constructor of the Controller class. Assign the input models and view to the private fields of the Controller
     * class and connect the ActionListener and MouseListener to the view. Display the GUI by calling the display method
     * in the View class.
     *
     * @param model The input model of the game.
     * @param view The input view of the game.
     */
    public Controller(Model model, View view){
        this.model = model;
        this.view = view;
        this.view.setListener(this, this);
        this.view.display();
    }

    /**
     * Clear the board and repaint the board.
     */
    @Override
    public void clearAndRepaintBoard(){
        model.clearBoard();
        view.updateCloneBoard(model.getCloneBoard());
        view.repaint();
        model.setTurn(0);
        model.setStep(0);
    }

    /**
     * Implement the actions of different button clicked event on the panel.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e){
        // When "New Game" button is clicked
        if(e.getActionCommand().equals("New Game")){
            // paint the empty board
            clearAndRepaintBoard();
            model.setTurn(1);
            if(model.getMode() == 2){
                runAIVsAI();
            }
        }
        // When the "Abort" button is clicked
        else if(e.getActionCommand().equals("Abort")){
            if(model.getTurn() == 1){
                view.popUp("White Wins");
            } else{
                view.popUp("Black wins");
            }
            clearAndRepaintBoard();
        }
        // When the "Author" button is clicked
        else if(e.getActionCommand().equals("Author")){
            String author = "Author: Yuting Shao\nGitHub:https://github.com/Yuting-Shao";
            view.popUp(author);
        }
        // When the "README" button is clicked
        else if(e.getActionCommand().equals("README")){
            String readme = "First select mode\nThen start New Game\nNote: AI uses minimax algorithm";
            view.popUp(readme);
        }
        // When the "Source" button is clicked
        else if(e.getActionCommand().equals("Source")){
            if(java.awt.Desktop.isDesktopSupported()){
                // If the system supports browser, then open the uri link in the browser
                try{
                    java.net.URI uri = java.net.URI.create("https://github.com/Yuting-Shao/FiveInARowAIWithMinimax");
                    java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
                    if(desktop.isSupported(Desktop.Action.BROWSE)){
                        desktop.browse(uri);
                    }
                } catch (java.io.IOException error){
                    error.printStackTrace();
                }
            }
        }
        // Set the model parameters according to the game mode selected in the box of the panel
        else if(view.getBoxes().getSelectedItem().equals("Human vs Human")){
            model.setMode(0);
            model.setTurn(0);
            model.setStep(0);
        }
        else if(view.getBoxes().getSelectedItem().equals("Human vs AI")){
            model.setMode(1);
            model.setTurn(0);
            model.setStep(0);
        }
        else if(view.getBoxes().getSelectedItem().equals("AI vs AI")){
            model.setMode(2);
            model.setTurn(0);
            model.setStep(0);
        }
    }

    /**
     * Run the AI vs AI game mode.
     */
    @Override
    public void runAIVsAI(){
        // AI vs AI
        // Place the first piece in the center of the board
        model.placeChessOnPosition(new Position(7,7),Player.X);
        model.setStep(model.getStep()+1);
        view.paintMark(model.getLastPosition().getRow(), model.getLastPosition().getColumn(), Player.X, model.getStep());
        model.setTurn(2);
        // Loop until there is a winner or it is a tie with no empty position anymore
        while(true) {
            if (model.getTurn() == 2) {
                //AI (O) turn
                oneAIStep(model.getH());
                model.setTurn(1);
                if (model.getWinner() == Player.O) {
                    view.popUp("AI White Wins!");
                    clearAndRepaintBoard();
                    break;
                }
                if(!model.checkEmpty()){
                    view.popUp("A tie!");
                    clearAndRepaintBoard();
                    break;
                }
                //AI (X) turn
                oneAIStep(model.getH());
                model.setTurn(2);
                if (model.getWinner() == Player.X) {
                    view.popUp("AI Black Wins!");
                    clearAndRepaintBoard();
                    break;
                }
                if(!model.checkEmpty()){
                    view.popUp("A tie!");
                    clearAndRepaintBoard();
                    break;
                }
            }
        }
    }

    /**
     * Moves one step of AI player.
     *
     * @param depth The input depth for the minimax algorithm
     * @throws IllegalArgumentException When the depth not positive
     */
    @Override
    public void oneAIStep(int depth)  throws IllegalArgumentException{
        if(depth <= 0){
            throw new IllegalArgumentException("The depth of the minimax algorithm must be positive.");
        }
        // Set the depth of the minimax algorithm
        model.setH(depth);
        model.moveAI();
        // Increase the step count after one move
        model.setStep(model.getStep()+1);
        // Paint the new piece on the board
        view.paintMark(model.getLastPosition().getRow(), model.getLastPosition().getColumn(), (model.getTurn() == 2) ? Player.O : Player.X, model.getStep());
    }

    /**
     * Handle the event when mouse clinking on the board. Get the clicking position and convert them to the index
     * of the board and input them to the go method, where corresponding actions will be performed according to
     * the game mode and player turn.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(java.awt.event.MouseEvent e){
        // Get the clicking position
        int x = e.getX();
        int y = e.getY();
        //Convert the position to board index
        x = (x / 40) * 40 + 20;
        y = (y / 40) * 40 + 20;
        int row = (y - 20) / 40;
        int column = (x - 20) / 40;
        //Pass the index to the go method
        if(row >=0 && row <= 14 && column >= 0 && column <= 14) {
            go(row, column);
        }
    }

    /**
     * Handle the event for the mouse pressing. This is must here because the implement of the MouseListener. But we
     * do not need it in the game so it is empty.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {

    }

    /**
     * Handle the event for the mouse releasing. This is must here because the implement of the MouseListener. But we
     * do not need it in the game so it is empty.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * Handle the event for the mouse entering. This is must here because the implement of the MouseListener. But we
     * do not need it in the game so it is empty.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Handle the event for the mouse exiting. This is must here because the implement of the MouseListener. But we
     * do not need it in the game so it is empty.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * Moves one step of human player.
     *
     * @param row The input row on the board
     * @param column The input column on the board
     * @throws IndexOutOfBoundsException When the index of row or column is not between 0 and 14
     */
    @Override
    public void oneHumanStep(int row, int column) throws IndexOutOfBoundsException{
        if(row < 0 || row > 14){
            throw new IndexOutOfBoundsException("Index of row must between 0 and 14.");
        }
        if(column < 0 || column > 14){
            throw new IndexOutOfBoundsException("Index of column must between 0 and 14.");
        }
        model.placeChessOnPosition(new Position(row, column), (model.getTurn() == 2) ? Player.O : Player.X);
        model.setStep(model.getStep()+1);
        view.paintMark(model.getLastPosition().getRow(), model.getLastPosition().getColumn(), (model.getTurn() == 2) ? Player.O : Player.X, model.getStep());
    }

    /**
     * Implement the actions when one specified position is clicking on the board based on the game mode and player turn.
     *
     * @param row The row index of the clicking position on the board
     * @param column The column index of the clicking position on the board
     * @throws IndexOutOfBoundsException When the index of row or column is not between 0 and 14
     */
    @Override
    public void go(int row, int column) throws IndexOutOfBoundsException{
        if(row < 0 || row > 14){
            throw new IndexOutOfBoundsException("Index of row must between 0 and 14.");
        }
        if(column < 0 || column > 14){
            throw new IndexOutOfBoundsException("Index of column must between 0 and 14.");
        }
        // If the game is started
        if(model.getTurn() != 0){
            if(model.getStateOnPosition(new Position(row, column)) != Player.EMPTY){
                view.popUp("Non-Empty position, try another position!");
            } else{
                if(model.getMode() == 0){
                    //Human vs Human
                    if(model.getTurn() == 1){
                        //Black (X) turn
                        oneHumanStep(row, column);
                        model.setTurn(2);
                        if(model.getWinner() == Player.X){
                            view.popUp("Black Wins!");
                            clearAndRepaintBoard();
                            return;
                        }
                        if(!model.checkEmpty()){
                            view.popUp("A tie!");
                            clearAndRepaintBoard();
                            return;
                        }
                    }
                    else if(model.getTurn() == 2){
                        //White (O) turn
                        oneHumanStep(row, column);
                        model.setTurn(1);
                        if(model.getWinner() == Player.O){
                            view.popUp("White Wins!");
                            clearAndRepaintBoard();
                            return;
                        }
                        if(!model.checkEmpty()){
                            view.popUp("A tie!");
                            clearAndRepaintBoard();
                            return;
                        }
                    }
                }
                else if(model.getMode() == 1){
                    //Human vs AI
                    if(model.getTurn() == 1){
                        //Black (X) turn
                        oneHumanStep(row, column);
                        model.setTurn(2);
                        if(model.getWinner() == Player.X){
                            view.popUp("You Wins!");
                            clearAndRepaintBoard();
                            return;
                        }
                        if(!model.checkEmpty()){
                            view.popUp("A tie!");
                            clearAndRepaintBoard();
                            return;
                        }
                        //AI (O) turn
                        oneAIStep(model.getH());
                        model.setTurn(1);
                        if(model.getWinner() == Player.O){
                            view.popUp("AI Wins!");
                            clearAndRepaintBoard();
                            return;
                        }
                        if(!model.checkEmpty()){
                            view.popUp("A tie!");
                            clearAndRepaintBoard();
                            return;
                        }
                    }
                }
            }
        }
    }
}
