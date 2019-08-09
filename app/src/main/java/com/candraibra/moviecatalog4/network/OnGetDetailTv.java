package com.candraibra.moviecatalog4.network;

import com.candraibra.moviecatalog4.model.Tv;

public interface OnGetDetailTv {
    void onSuccess(Tv tv);

    void onError();
}
