export type Type = "primary" | "secondary" | "success" | "error" | "highlight" | "light";
export type Size = "big" | "medium" | "normal" | "small" | "tiny";
export type ButtonType = Exclude<Type, 'success' | 'highlight' | 'light'> | 'icon' | 'transparent' | 'trash';
export type DropdownType = Exclude<Type, 'success' | 'highlight' | 'light'> | 'icon';
export type InputType = "text" | "number" | "date" | "email" | "search" | "tel" | "password";
export type Placement = "auto" | "auto-start" | "auto-end" | "top" | "top-start" | "top-end" | "bottom" | "bottom-start" | "bottom-end" | "right" | "right-start" | "right-end" | "left" | "left-start" | "left-end";
