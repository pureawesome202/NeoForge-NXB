package net.narutoxboruto.attachments.info;

import com.mojang.serialization.Codec;
import net.narutoxboruto.networking.info.SyncClan;

public class Clan {
    private String value;

    public static final Codec<Clan> CODEC = Codec.STRING.xmap(Clan::new, Clan::getValue);


    public Clan() {
        this.value = "clan";
    }

    public Clan(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Object getSyncMessage() {
        return new SyncClan(getValue());
    }
}
