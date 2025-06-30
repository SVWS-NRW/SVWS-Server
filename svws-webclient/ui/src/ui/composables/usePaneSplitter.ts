import { computed, ref, type Ref } from 'vue';

export interface PaneSplitterConfig {
	defaultSplit?: number;
	minSplit?: number;
	maxSplit?: number;
	snap?: number;
	unit?: string;
	mode?: 'vertical' | 'horizontal';
	dragger?: Ref<HTMLElement | null>;
}

/**
 * Diese Funktion stellt einen PaneSplitter zur Verfügung, bzw. die Mittel, um einen zu verwenden
 * Dazu werden ein paar Config-Daten übergeben, z.B. die Ausgangsbreite des Panels, die Mindest- und Maximalgröße eines Panels.
 * Dazu, ob es ein horizontaler oder ein vertikaler Split sein soll. Unis werden aktuell nicht berücksichtigt.
 */
export function usePaneSplitter({ defaultSplit= 80, minSplit= 20, maxSplit= 80, snap= 100, unit= '%', mode= 'vertical', dragger= ref(null) }: PaneSplitterConfig= {}) {

	const currentSplit = ref(defaultSplit);

	const boundSplit = computed(() => {
		if (currentSplit.value < minSplit)
			return minSplit;
		else if (currentSplit.value > maxSplit)
			return maxSplit;
		else if (currentSplit.value >= (maxSplit * (snap / 100)))
			return maxSplit;
		else
			return currentSplit.value;
	});

	const thisStyle = computed(() => `${mode === 'vertical' ? 'width':'height'}: ${boundSplit.value}%;`);
	const thatStyle = computed(() => `${mode === 'vertical' ? 'width':'height'}: ${100 - boundSplit.value}%;`);

	const dragging = ref(false)
	let startPosition = 0;
	let startSplit = 0;

	function dragStart(e: MouseEvent) {
		dragging.value = true;
		startPosition = mode === 'vertical' ? e.pageX : e.pageY;
		startSplit = boundSplit.value;
		window.addEventListener('mousemove', dragMove);
		window.addEventListener('mouseup', dragEnd);
	}

	function dragMove(e: MouseEvent) {
		if (dragging.value && (dragger.value !== null)) {
			let position;
			let totalSize;
			if (mode === 'vertical') {
				position = e.pageX;
				totalSize = dragger.value.offsetWidth;
			}
			else {
				position = e.pageY;
				totalSize = dragger.value.offsetHeight;
			}
			const dPosition = position - startPosition;
			currentSplit.value = startSplit + (~~(dPosition / totalSize * 200) / 2);
		}
	}

	function dragEnd() {
		dragging.value = false;
		removeDragListeners();
	}

	function removeDragListeners() {
		window.removeEventListener('mousemove', dragMove);
		window.removeEventListener('mouseup', dragEnd);
	}

	return { removeDragListeners, dragStart, thisStyle, thatStyle, dragger };
}
