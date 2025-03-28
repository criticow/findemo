import { MaskProcessor } from '../mask.directive';

const format = (value: string) => {
  if(value == null) return init();

  value = String(value).replace(/\D/g, '');

  let numericValue = parseFloat(value) / 100;

  let formattedValue = numericValue.toLocaleString('pt-BR', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  });

  return formattedValue;
};

const convert = (value: string) => {
  value = (value || '').replace(/\D/g, '');
  let numericValue = parseFloat(value) / 100;
  return numericValue;
};

const validate = (value: number) => {
  if (value === null || value === undefined || isNaN(value)) {
    return { invalidAmount: 'Valor inválido' };
  }

  if (value < 0.01) {
    return { minAmount: 'O valor mínimo é 0,01' };
  }

  return null; // Sem erro
};

const init = () => {
  return '0,00';
}

export const amountProcessor: MaskProcessor = {
  format,
  convert,
  init,
  validate,
};