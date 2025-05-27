// Game.java
// This class represents a Game in our application.
// It holds information about a game, such as its name, price, and genre.
public class Game {
    // This variable stores the name of the game as a String.
    private String name;
    // This variable stores the price of the game as a double.
    private double price;
    // This variable stores the genre of the game, also as a String.
    private String genre;

    // This is the constructor for the Game class.
    // It takes name, price, and genre as parameters to initialize a new Game object.
    public Game(String name, double price, String genre) {
        // Assign the provided name to the instance variable 'name'.
        this.name = name;
        // Assign the provided price to the instance variable 'price'.
        this.price = price;
        // Assign the provided genre to the instance variable 'genre'.
        this.genre = genre;
    }

    // This method returns the name of the game.
    public String getName() {
        // Return the value of the 'name' instance variable.
        return name;
    }

    // This method sets the name of the game.
    // It takes a String 'name' as a parameter.
    public void setName(String name) {
        // Update the 'name' instance variable with the new name provided.
        this.name = name;
    }

    // This method returns the price of the game.
    public double getPrice() {
        // Return the value of the 'price' instance variable.
        return price;
    }

    // This method sets the price of the game.
    // It takes a double 'price' as a parameter.
    public void setPrice(double price) {
        // Update the 'price' instance variable with the new price provided.
        this.price = price;
    }

    // This method returns the genre of the game.
    public String getGenre() {
        // Return the value of the 'genre' instance variable.
        return genre;
    }

    // This method sets the genre of the game.
    // It takes a String 'genre' as a parameter.
    public void setGenre(String genre) {
        // Update the 'genre' instance variable with the new genre provided.
        this.genre = genre;
    }

    // This is the toString method, which provides a string representation of the Game object.
    // It's often used for debugging or logging.
    @Override
    public String toString() {
        // Return a formatted string containing the game's details.
        return "Game{name='" + name + "', price=" + price + ", genre='" + genre + "'}";
    }
}
