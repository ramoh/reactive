import { useState } from "react";

export const useForm = ({ initialValue = {} }) => {
  const [values, setValues] = useState(initialValue);
  const [invalid, setInvalid] = useState({});
  const [error, setError] = useState("");

  const setValue = (name, value) => {
    setValues({ ...values, [name]: value });
  };

  const onChange = event => {
    const { name, value } = event.currentTarget;
    setError("");
    setInvalid({ ...invalid, [name]: !event.currentTarget.checkValidity() });
    setValue(name, value);
  };

  const isValid = Object.values(invalid).every(value => value === false);

  const clearForm = () => {
    setValues(initialValue);
    setInvalid({});
    setError("");
  };

  return { values, setValues, isValid, invalid, error, setError, clearForm, onChange };
};