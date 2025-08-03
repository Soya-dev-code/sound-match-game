public class Question {
    private String soundFile;
    private String[] options;
    private String correctAnswer;

    public Question(String soundFile, String[] options, String correctAnswer) {
        this.soundFile = soundFile;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getSoundFile() {
        return soundFile;
    }

    public String[] getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}