import { FileDTO } from "../files/files";

/** Flat node with expandable and level information */
export class DynamicFlatNode {
    constructor(
        public item: FileDTO,
        public level = 1,
        public expandable = false,
        public isLoading = false,
    ) { }
}

export interface TreeNode {
    item: string;
    isFolder: boolean
    children?: TreeNode[];
    isLoading?: boolean;
    level: number;
}