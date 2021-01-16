package dscp.dragon_realm.dataContainer;

import com.google.common.primitives.Doubles;
import dscp.dragon_realm.bounty.Bounty;
import dscp.dragon_realm.currency.PlayerWallet;
import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public interface DataContainerDataType<T> {
    BaseDataType<Integer> IntegerType = new BaseDataType<>(Integer.class, "Integers");
    BaseDataType<Double> DoubleType = new BaseDataType<>(Double.class, "Doubles");
    BaseDataType<String> StringType = new BaseDataType<>(String.class, "Strings");
    BaseDataType<Long> LongType = new BaseDataType<>(Long.class, "Longs");
    BaseDataType<UUID> UUIDType = new BaseDataType<>(UUID.class, "UUID's");

    BaseDataType<Bounty> BountyType = new BaseDataType<>(Bounty.class, "Bounty");

    BaseDataType<PlayerWallet> PlayerWalletType = new BaseDataType<>(PlayerWallet.class, "Player Wallet");


    Class<T> getType();

    String getName();

    class BaseDataType<T> implements DataContainerDataType<T>, Serializable {
        private static final long serialVersionUID = -2391123945662368971L;

        private final Class<T> type;
        private final String name;

        private BaseDataType(Class<T> type, String name){
            this.type = type;
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Class<T> getType() {
            return type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BaseDataType)) return false;
            BaseDataType<?> that = (BaseDataType<?>) o;
            return Objects.equals(type, that.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type);
        }
    }
}
