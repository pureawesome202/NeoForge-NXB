package net.narutoxboruto.attachments.info;

import com.mojang.serialization.Codec;
import net.narutoxboruto.networking.info.SyncAffiliation;

public class Affiliation {
    private String value;

    public static final Codec<Affiliation> CODEC = Codec.STRING.xmap(Affiliation::new, Affiliation::getValue);


    public Affiliation() {
        this.value = "affiliation";
    }

    public Affiliation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Object getSyncMessage() {
        return new SyncAffiliation(getValue());
    }
}
