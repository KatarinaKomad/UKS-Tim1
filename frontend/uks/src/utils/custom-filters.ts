import { FormInput } from "src/models/forms/input"

export const formFilter = (formInputs: FormInput[], id: string): any => {
    return formInputs.filter(input => input.id === id).at(0)?.control.value
}