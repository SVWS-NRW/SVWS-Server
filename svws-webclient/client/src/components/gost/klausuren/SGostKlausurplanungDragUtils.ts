import { ref } from "vue";
import type { GostKlausurtermin } from "@core";
import type { GostKlausurplanungDragData } from "./SGostKlausurplanung";

export type KlausurplanungDropState = "none" | "valid" | "danger" | "disabled";

type KlausurplanungDropStateOptions = {
	hasDragData: boolean;
	canDrop: boolean;
	hasConflict?: boolean;
	showDisabled?: boolean;
};

// Übersetzt Drag- und Konfliktinformationen in den visuellen Zustand einer Drop-Zone.
export function klausurplanungDropState({ hasDragData, canDrop, hasConflict = false, showDisabled = true }: KlausurplanungDropStateOptions): KlausurplanungDropState {
	if (!hasDragData) {
		return "none";
	}
	if (!canDrop) {
		return showDisabled ? "disabled" : "none";
	}
	return hasConflict ? "danger" : "valid";
}

// Bündelt den lokalen Drag-State einer Klausurplanungsansicht und optionale Seiteneffekte bei Drag-Wechseln.
export function useKlausurplanungDragAndDrop(
	onDragChange?: (data: GostKlausurplanungDragData) => void
) {
	const dragData = ref<GostKlausurplanungDragData>(undefined);

	// Setzt das aktuell gezogene Objekt und informiert die aufrufende Ansicht über den Wechsel.
	function setDragData(data: GostKlausurplanungDragData): void {
		dragData.value = data;
		onDragChange?.(data);
	}

	// Einheitlicher Handler für dragstart/dragend; das Event selbst wird aktuell nicht benötigt.
	function onDrag(_event: DragEvent | undefined, data: GostKlausurplanungDragData): void {
		setDragData(data);
	}

	// Scrollt den ausgewählten Termin in Sidebar-Listen nach, wenn er programmatisch gewechselt wurde.
	function scrollSelectedTerminIntoView(termin: GostKlausurtermin | undefined): void {
		if (termin === undefined) {
			return;
		}
		const scrollToElement = document.getElementById("termin" + termin.id);
		if (scrollToElement) {
			scrollToElement.scrollIntoView({ behavior: "smooth", block: "nearest" });
		}
	}

	return {
		dragData,
		onDrag,
		scrollSelectedTerminIntoView,
		setDragData,
	};
}
