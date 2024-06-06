import java.util.Arrays;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;
import static java.util.concurrent.ForkJoinTask.invokeAll;

public class LogicaForkJoin {
    public static void ForkJoin(int[] array) {
        ForkJoinPool pool = new ForkJoinPool();
        MergeSortTask task = new MergeSortTask(array);
        pool.invoke(task);
    }

    private static class MergeSortTask extends RecursiveAction {
        private int[] array;

        public MergeSortTask(int[] array) {
            this.array = array;
        }

        @Override
        protected void compute() {
            if (array.length > 1) {
                int mid = array.length / 2;
                int[] left = Arrays.copyOfRange(array, 0, mid);
                int[] right = Arrays.copyOfRange(array, mid, array.length);

                MergeSortTask leftTask = new MergeSortTask(left);
                MergeSortTask rightTask = new MergeSortTask(right);

                invokeAll(leftTask, rightTask);

                merge(array, left, right);
            }
        }

        private void merge(int[] result, int[] left, int[] right) {
            int i = 0, j = 0, k = 0;
            while (i < left.length && j < right.length) {
                if (left[i] <= right[j]) {
                    result[k++] = left[i++];
                } else {
                    result[k++] = right[j++];
                }
            }
            while (i < left.length) {
                result[k++] = left[i++];
            }
            while (j < right.length) {
                result[k++] = right[j++];
            }
        }
    }
}

