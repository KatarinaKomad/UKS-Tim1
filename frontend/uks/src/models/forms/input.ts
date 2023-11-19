import { FormControl, FormGroup } from '@angular/forms';

export type FormInput = {
  id: string;
  placeholder: string;
  label: string;
  required: boolean;
  errorMessage: string;
  control: FormControl;
  type: string;
};

export function createFormGroupFromFormConfig(config: FormInput[]) {
  const formGroupConfig: any = {};
  config.forEach((formInput) => {
    formGroupConfig[formInput.id] = formInput.control;
  });

  return formGroupConfig;
}
