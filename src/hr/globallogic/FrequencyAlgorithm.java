package hr.globallogic;

import java.util.*;
import java.util.stream.Collectors;

public class FrequencyAlgorithm {
    private Set<Character> specialCharacters;
    private Set<Character> wantedCharacters;

    public FrequencyAlgorithm(String specialCharacters, String wantedCharacters) {
        this.specialCharacters = stringToSet(specialCharacters);
        this.wantedCharacters = stringToSet(wantedCharacters);
    }

    public Set<Character> getSpecialCharacters() {
        return specialCharacters;
    }

    public void setSpecialCharacters(Set<Character> specialCharacters) {
        this.specialCharacters = specialCharacters;
    }

    public Set<Character> getWantedCharacters() {
        return wantedCharacters;
    }

    public void setWantedCharacters(Set<Character> wantedCharacters) {
        this.wantedCharacters = wantedCharacters;
    }

    /**
     * Creates a Set representation of characters given a string.
     * @param characters String representation of characters that will be added into Set
     * @return Set<Characters> based on the given String
     */
    private static Set<Character> stringToSet(String characters) {
        Set<Character> characterSet = new HashSet<>();
        for(char c : characters.toLowerCase().toCharArray()) {
            characterSet.add(c);
        }
        return characterSet;
    }

    /**
     * Returns length of the word without counting occurrences of special characters
     * @param word Word, part of a phrase given by the user as input
     * @param specials Set of characters that don't come into consideration when calculating frequency
     * @return Length of the word without counting occurrences of special characters
     */
    private static int countWithoutSpecialCharacters(String word, Set<Character> specials) {
        int counter = 0;
        for(char c : word.toCharArray()) {
            if (!specials.contains(c)) {
                counter++;
            }
        }
        return counter;
    }

    /**
     * Executes the algorithm for identifying frequency of wanted characters in phrase
     * @param phrase String representation of the input provided by user
     * @param wantedCharacters Set of characters whose frequency needs to be found
     * @param specialCharacters Set of characters that don't come into consideration when calculating frequency
     * @return Map<Pair<Set<Character>, Integer>, Double> in which Pair is a key in which value A represents a set of characters
     * found in the word and value B represents the length of the word. The value of the map is a Double value which represents the
     * number of appearances of any of the wanted characters in the word (group).
     */
    private static Map<Pair<Set<Character>, Integer>, Double> computeFrequency(String phrase,
                                                                               Set<Character> wantedCharacters, Set<Character> specialCharacters) {
        Map<Pair<Set<Character>, Integer>, Double> groups = new HashMap<>();

        for(String word : phrase.split(" ")) {
            Set<Character> combination = new HashSet<>();
            int charCount = 0;
            for(char c : word.toCharArray()) {
                if (wantedCharacters.contains(c)) {
                    combination.add(c);
                    charCount++;
                }
            }
            if (combination.size() > 0) {
                Pair<Set<Character>, Integer> pair = new Pair<>(combination, countWithoutSpecialCharacters(word, specialCharacters));
                if (groups.containsKey(pair)) {
                    groups.put(pair, groups.get(pair) + charCount);
                }
                else {
                    groups.put(pair, (double) charCount);
                }
            }
        }

        return groups;
    }

    /**
     * Returns a map sorted by values in ascending order
     * @param map with Pair<Set<Character>, Integer> as key and Double (frequency) as value
     * @return map sorted by values in ascending order
     */
    private static Map<Pair<Set<Character>, Integer>, Double> sortByValue(Map<Pair<Set<Character>, Integer>, Double> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    /**
     * Prints the result of the analysis
     * @param computedGroups Map with algorithm results
     * @param wantedCharCounter Number of appearances of wanted characters in the given phrase
     * @param allowedCharCounter Number of allowed character appearances in the given phrase
     */
    private static String printResult(Map<Pair<Set<Character>, Integer>, Double> computedGroups,
                                    int wantedCharCounter, int allowedCharCounter) {
        StringBuilder output = new StringBuilder();

        for (Map.Entry<Pair<Set<Character>, Integer>, Double> group : computedGroups.entrySet()) {
            output.append(group.getKey())
                    .append(" = ")
                    .append(Math.round(group.getValue() / wantedCharCounter * 100.0) / 100.0)
                    .append(" (")
                    .append(Math.round(group.getValue()))
                    .append("/")
                    .append(wantedCharCounter)
                    .append(")\n");
        }
        output.append("TOTAL Frequency: ")
                .append(Math.round((double) wantedCharCounter / allowedCharCounter * 100.0) / 100.0)
                .append(" (").append(wantedCharCounter)
                .append("/")
                .append(allowedCharCounter)
                .append(")\n");

        return output.toString();
    }

    /**
     * Executes the algorithm for a provided phrase
     * @param phrase String representation on which the algorithm is going to be executed
     */
    public String Execute(String phrase) {
        int onlyAllowedCharsCounter = 0;
        int onlyWantedCharsCounter = 0;

        // Counts the appearances of allowed and wanted characters
        for(char c : phrase.toLowerCase().toCharArray()) {
            if (!getSpecialCharacters().contains(c)) {
                onlyAllowedCharsCounter++;
                if (getWantedCharacters().contains(c)) {
                    onlyWantedCharsCounter++;
                }
            }
        }

        // Executes the algorithm and sorts the map
        Map<Pair<Set<Character>, Integer>, Double> computedGroups =
                sortByValue(computeFrequency(phrase.toLowerCase(), getWantedCharacters(), getSpecialCharacters()));

        return printResult(computedGroups, onlyWantedCharsCounter, onlyAllowedCharsCounter);
    }
}
