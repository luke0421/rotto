import { Picker } from "@react-native-picker/picker";
import { useState } from "react";

const MyPicker = ({ data }) => {
  const [selectedItem, setSelectedItem] = useState();
  return (
    <Picker
      selectedValue={selectedItem}
      onValueChange={(itemValue, itemIndex) => setSelectedItem(itemValue)
      }
    >
      {data.map((item) => {
        return <Picker.item key={item.index} label={item.label} value={item.value} />;
      })}
    </Picker>
  );
};

export default MyPicker;
