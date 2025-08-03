import java.util.*;

public class QuestionBank {
     private List<Question> questions;

    public QuestionBank() {
        questions = new ArrayList<>();
        addQuestions();
    }

    private void addQuestions() {
        questions.add(new Question("ambulance.mp3", new String[]{"ambulance", "train", "rickshaw"}, "ambulance"));
        questions.add(new Question("bike.mp3", new String[]{"car", "bike", "jcb"}, "bike"));
        questions.add(new Question("birdchirping.mp3", new String[]{"cat", "bird", "lion"}, "bird"));
        questions.add(new Question("car.mp3", new String[]{"bike", "car", "jcb"}, "car"));
        questions.add(new Question("catmeowing.mp3", new String[]{"dog", "cat", "cow"}, "cat"));
        questions.add(new Question("cellphone-ringing.mp3", new String[]{"phone", "alarm", "bell"}, "phone"));
        questions.add(new Question("clapping.mp3", new String[]{"drumming", "clapping", "laughing"}, "clapping"));
        questions.add(new Question("coughing.mp3", new String[]{"sneezing", "laughing", "coughing"}, "coughing"));
        questions.add(new Question("cowmooing.mp3", new String[]{"dog", "cow", "lion"}, "cow"));
        questions.add(new Question("crying.mp3", new String[]{"baby", "laughing", "crying"}, "crying"));
        questions.add(new Question("dogbarking.mp3", new String[]{"cat", "dog", "lion"}, "dog"));
        questions.add(new Question("doorbell.mp3", new String[]{"phone", "doorbell", "alarm"}, "doorbell"));
        questions.add(new Question("elephanttrumpeting.mp3", new String[]{"elephant", "cow", "train"}, "elephant"));
        questions.add(new Question("firecrackling.mp3", new String[]{"fire", "rain", "wind"}, "fire"));
        questions.add(new Question("flute.mp3", new String[]{"piano", "guitar", "flute"}, "flute"));
        questions.add(new Question("guitar.mp3", new String[]{"flute", "guitar", "piano"}, "guitar"));
        questions.add(new Question("jcb.mp3", new String[]{"train", "jcb", "bike"}, "jcb"));
        questions.add(new Question("laughing.mp3", new String[]{"crying", "laughing", "clapping"}, "laughing"));
        questions.add(new Question("lionroaring.mp3", new String[]{"dog", "lion", "cat"}, "lion"));
        questions.add(new Question("oceanwaves.mp3", new String[]{"rain", "ocean", "wind"}, "ocean"));
        questions.add(new Question("piano.mp3", new String[]{"piano", "flute", "guitar"}, "piano"));
        questions.add(new Question("rickshaw.mp3", new String[]{"train", "rickshaw", "bike"}, "rickshaw"));
        questions.add(new Question("runningwater.mp3", new String[]{"rain", "river", "running water"}, "running water"));
        questions.add(new Question("sneezing.mp3", new String[]{"sneezing", "coughing", "crying"}, "sneezing"));
        questions.add(new Question("ticking-clock.mp3", new String[]{"clock", "machine", "alarm"}, "clock"));
        questions.add(new Question("train.mp3", new String[]{"car", "jcb", "train"}, "train"));
        questions.add(new Question("windblowing.mp3", new String[]{"wind", "ocean", "fire"}, "wind"));

    }
    public List<Question> getAllQuestions() {
        return questions;
    }

    public List<Question> getShuffledQuestions() {
        List<Question> shuffled = new ArrayList<>(questions);
        Collections.shuffle(shuffled);
        return shuffled;
    }
}