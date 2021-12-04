package cn.hikyson.godeye.core.internal.modules.viewcanary.levenshtein;

import java.util.List;

/**
 * https://github.com/tdebatty/java-string-similarity#weighted-levenshtein
 */
public class WeightedLevenshtein {

    private final ViewWithSizeSubstitutionInterface charsub;
    private final ViewWithSizeInsDelInterface charchange;

    /**
     * Instantiate with provided character substitution.
     *
     * @param charsub The strategy to determine character substitution weights.
     */
    public WeightedLevenshtein(final ViewWithSizeSubstitutionInterface charsub) {
        this(charsub, null);
    }

    /**
     * Instantiate with provided character substitution, insertion, and
     * deletion weights.
     *
     * @param charsub    The strategy to determine character substitution weights.
     * @param charchange The strategy to determine character insertion /
     *                   deletion weights.
     */
    public WeightedLevenshtein(final ViewWithSizeSubstitutionInterface charsub,
                               final ViewWithSizeInsDelInterface charchange) {
        this.charsub = charsub;
        this.charchange = charchange;
    }

    /**
     * Equivalent to distance(state1, state2, Double.MAX_VALUE).
     */
    public final double distance(final List<ViewIdWithSize> state1, final List<ViewIdWithSize> state2) {
        return distance(state1, state2, Double.MAX_VALUE);
    }

    /**
     * Compute Levenshtein distance using provided weights for substitution.
     *
     * @param state1    The first string to compare.
     * @param state2    The second string to compare.
     * @param limit The maximum result to compute before stopping. This
     *              means that the calculation can terminate early if you
     *              only care about strings with a certain similarity.
     *              Set this to Double.MAX_VALUE if you want to run the
     *              calculation to completion in every case.
     * @return The computed weighted Levenshtein distance.
     * @throws NullPointerException if state1 or state2 is null.
     */
    public final double distance(final List<ViewIdWithSize> state1, final List<ViewIdWithSize> state2,
                                 final double limit) {
        if (state1 == null) {
            throw new NullPointerException("state1 must not be null");
        }

        if (state2 == null) {
            throw new NullPointerException("state2 must not be null");
        }

        if (state1.equals(state2)) {
            return 0;
        }

        if (state1.size() == 0) {
            return state2.size();
        }

        if (state2.size() == 0) {
            return state1.size();
        }

        // create two work vectors of floating point (i.e. weighted) distances
        double[] vector0 = new double[state2.size() + 1];
        double[] vector1 = new double[state2.size() + 1];
        double[] vtemp;

        // initialize vector0 (the previous row of distances)
        // this row is A[0][i]: edit distance for an empty state1
        // the distance is the cost of inserting each character of state2
        vector0[0] = 0;
        for (int i = 1; i < vector0.length; i++) {
            vector0[i] = vector0[i - 1] + insertionCost(state2.get(i - 1));
        }

        for (int i = 0; i < state1.size(); i++) {
            ViewIdWithSize s1i = state1.get(i);
            double deletion_cost = deletionCost(s1i);

            // calculate vector1 (current row distances) from the previous row vector0
            // first element of vector1 is A[i+1][0]
            // Edit distance is the cost of deleting characters from state1
            // to match empty t.
            vector1[0] = vector0[0] + deletion_cost;

            double minv1 = vector1[0];

            // use formula to fill in the rest of the row
            for (int j = 0; j < state2.size(); j++) {
                ViewIdWithSize s2j = state2.get(j);
                double cost = 0;
                if (!s1i.equals(s2j)) {
                    cost = charsub.cost(s1i, s2j);
                }
                double insertion_cost = insertionCost(s2j);
                vector1[j + 1] = Math.min(
                        vector1[j] + insertion_cost, // Cost of insertion
                        Math.min(
                                vector0[j + 1] + deletion_cost, // Cost of deletion
                                vector0[j] + cost)); // Cost of substitution

                minv1 = Math.min(minv1, vector1[j + 1]);
            }

            if (minv1 >= limit) {
                return limit;
            }

            // copy vector1 (current row) to vector0 (previous row) for next iteration
            //System.arraycopy(vector1, 0, vector0, 0, vector0.length);
            // Flip references to current and previous row
            vtemp = vector0;
            vector0 = vector1;
            vector1 = vtemp;

        }

        return vector0[state2.size()];//FDS ix all short variable s1 s2 character
    }

    private double insertionCost(final ViewIdWithSize character) {
        if (charchange == null) {
            return 1.0;
        } else {
            return charchange.insertionCost(character);
        }
    }

    private double deletionCost(final ViewIdWithSize character) {
        if (charchange == null) {
            return 1.0;
        } else {
            return charchange.deletionCost(character);
        }
    }
}
