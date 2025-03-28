import { MaskProcessor } from '../mask.directive';

const format = (value: string) => {
  return value
    .replace(/\D/g, '')
    .replace(/(\d{2})(\d)/, '$1\/$2')
    .replace(/(\d{2})(\d)/, '$1\/$2')
    .replace(/(\/\d{4})\d+?$/, '$1');
};

const convert = (value: string) => {
  return value.split("/").reverse().join("-");
};

const init = () => {
  const date = new Date();

  const [day, month, year] = [
    date.getDate(),
    date.getMonth() + 1,
    date.getFullYear()
  ];

  return `${day.toString().padStart(2, '0')}/${month.toString().padStart(2, '0')}/${year}`;
};

const validate = (value: string) => {
  if (!value || value.length < 10) {
    return { invalidLength: 'Data incompleta' };
  }

  const [year, month, day] = convert(value)
    .split("-")
    .map((segment) => Number(segment));

  const date = new Date(year, month - 1, day);

  if(
    year !== date.getFullYear() ||
    (month - 1) !== date.getMonth() ||
    day !== date.getDate()
  )
  {
    return { invalidDate: 'Data inválida' };
  }

  return null;
};

export const dateProcessor: MaskProcessor = {
  format,
  convert,
  init,
  validate
};