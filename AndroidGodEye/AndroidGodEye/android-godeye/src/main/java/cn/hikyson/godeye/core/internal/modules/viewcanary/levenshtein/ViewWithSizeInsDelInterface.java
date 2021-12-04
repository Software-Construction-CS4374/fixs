package cn.hikyson.godeye.core.internal.modules.viewcanary.levenshtein;

public interface ViewWithSizeInsDelInterface {
    double deletionCost(ViewIdWithSize cost);//FDS fix short variable line 4 and 6

    double insertionCost(ViewIdWithSize cost);
}
