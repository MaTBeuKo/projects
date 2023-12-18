package expression;

import java.util.HashMap;
import java.util.Map;

public class Priority {
    static int getPriority(Expression expression){
        if (expression instanceof Add){
            return 1;
        }else if (expression instanceof Subtract){
            return 1;
        } else if (expression instanceof Multiply){
            return 2;
        }else if (expression instanceof Divide){
            return 2;
        }else if (expression instanceof Const){
            return 3;
        }else if (expression instanceof Variable){
            return 3;
        }
        assert (false);
        return 4;
    }
}
