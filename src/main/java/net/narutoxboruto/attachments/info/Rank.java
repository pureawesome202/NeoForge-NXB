package net.narutoxboruto.attachments.info;

import com.mojang.serialization.Codec;
import net.narutoxboruto.networking.info.SyncRank;

public class Rank {
    private String value;

    public static final Codec<Rank> CODEC = Codec.STRING.xmap(Rank::new, Rank::getValue);


    public Rank() {
        this.value = "rank";
    }

    public Rank(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Object getSyncMessage() {
        return new SyncRank(getValue());
    }

}
