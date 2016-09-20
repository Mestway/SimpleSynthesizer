package sql.lang.ast.filter;

import forward_enumeration.primitive.parameterized.InstantiateEnv;
import sql.lang.ast.Environment;
import sql.lang.ast.Hole;
import sql.lang.exception.SQLEvalException;
import sql.lang.trans.ValNodeSubstBinding;
import util.IndentionManagement;

import java.util.List;

/**
 * Created by clwang on 12/20/15.
 */
public class LogicOrFilter implements Filter {

    Filter f1;
    Filter f2;

    public LogicOrFilter(Filter f1, Filter f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    @Override
    public boolean filter(Environment env) throws SQLEvalException {
        return f1.filter(env) || f2.filter(env);
    }

    @Override
    public int getFilterLength() {
        return f1.getFilterLength() + f2.getFilterLength();
    }

    @Override
    public int getNestedQueryLevel() {
        return f1.getNestedQueryLevel() > f2.getNestedQueryLevel()?
                f1.getNestedQueryLevel() : f2.getNestedQueryLevel();
    }

    @Override
    public String prettyPrint(int indentLv) {
        String result = f1.prettyPrint(0) + "\r\n OR " + f2.prettyPrint(0);
        return IndentionManagement.addIndention(result, indentLv);
    }

    @Override
    public boolean containsExclusiveFilter(Filter f) {
        return false;
    }

    @Override
    public List<Hole> getAllHoles() {
        List<Hole> result = f1.getAllHoles();
        result.addAll(f2.getAllHoles());
        return result;
    }

    @Override
    public Filter instantiate(InstantiateEnv env) {
        return new LogicOrFilter(f1.instantiate(env), f2.instantiate(env));
    }

    @Override
    public Filter substNamedVal(ValNodeSubstBinding vnsb) {
        return new LogicOrFilter(f1.substNamedVal(vnsb), f2.substNamedVal(vnsb));
    }

}
