import React, { useState, useEffect } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSave } from '@fortawesome/free-solid-svg-icons';
import Alert from './Alert';
import BackendService from '../services/BackendService';
import { useParams, useNavigate } from 'react-router-dom';

const CountryComponent = () => {
  const { id } = useParams(); // Получаем параметр id из URL
  const navigate = useNavigate();

  const [country, setCountry] = useState({});
  const [showAlert, setShowAlert] = useState(false);

  useEffect(() => {
    retrieveCountry();
  }, []);

  const retrieveCountry = () => {
    BackendService.retrieveCountry(id)
      .then((resp) => {
        setCountry(resp.data);
      })
      .catch((error) => {
        console.error('Ошибка при получении данных страны:', error);
      });
  };

  const updateCountry = () => {
    BackendService.updateCountry(country)
      .then(() => {
        // Обновление страны выполнено успешно
        navigate('/countries'); // Переход на страницу со списком стран
      })
      .catch((error) => {
        console.error('Ошибка при обновлении страны:', error);
        setShowAlert(true); // Отображение сообщения об ошибке
      });
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setCountry((prevCountry) => ({
      ...prevCountry,
      [name]: value,
    }));
  };

  const closeAlert = () => {
    setShowAlert(false);
  };

  return (
    <div className="m-4">
      <h3>Страна: {country.name}</h3>
      <div className="row my-2">
        <div className="col">
          <label htmlFor="name">Название страны:</label>
          <input
            type="text"
            id="name"
            name="name"
            value={country.name || ''}
            onChange={handleInputChange}
            className="form-control"
          />
        </div>
      </div>
      <div className="row my-2">
        <div className="col">
          <button className="btn btn-primary" onClick={updateCountry}>
            <FontAwesomeIcon icon={faSave} /> Сохранить
          </button>
        </div>
      </div>
      <Alert
        title="Ошибка"
        message="Произошла ошибка при обновлении страны."
        ok={closeAlert}
        close={closeAlert}
        modal={showAlert}
      />
    </div>
  );
};

export default CountryComponent;
