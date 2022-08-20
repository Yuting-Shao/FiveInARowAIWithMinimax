/**
 * The main program class for the Five In A Row game with the AI player supported by the minimax algorithm alpha-beta
 * pruning.
 */
public class FiveInARow {
    public static void main(String[] args){
        Model model = new Model();
        View view = new View("Five In A Row");
        //The constructor of the Controller class will start the game
        new Controller(model, view);
    }
}
