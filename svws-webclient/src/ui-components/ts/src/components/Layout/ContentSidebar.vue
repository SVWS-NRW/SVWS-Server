<script setup lang='ts'>
import { debounce } from "debounce";

type Size = 'normal' | 'large';

const {
	size = 'normal'
} = defineProps<{
	size?: Size;
}>();

const main = ref<HTMLElement | null>(null);
const sidebar = ref<HTMLElement | null>(null);
const mainWidth = ref(0);
const sidebarWidth = ref(0);
const sizeMultiplier = ref(0);

onMounted(() => {
	main.value = document.querySelector("main");

	switch (size) {
		case "normal":
			sizeMultiplier.value = 0.33;
			break;
		case "large":
			sizeMultiplier.value = 0.66;
			break;
		default:
			sizeMultiplier.value = 0.33;
	}
	// Initial Breite setzen
	handleWidth();
	// Eventlistener für das Resize-Event mit einem Debounce von 100ms um Resourcen des Anwenders zu schonen
	window.addEventListener("resize", debounce(handleWidth, 100));
});

onUnmounted(() => {
	window.removeEventListener("resize", handleWidth);
})


function handleWidth() {
	mainWidth.value = main.value?.clientWidth ?? 0;
	// Die Breite der Sidebar wird errechnet durch die Breite des Main-Bereichs multipliziert mit dem Faktor der Size-Prop.
	sidebarWidth.value = Math.floor(
		mainWidth.value * sizeMultiplier.value
	);
	// Die Sidebar sollte eine Mindestbreite von 17.5rem haben damit eine vernünftige Nutzung gewährleistet wird.
	const minWidth = 17.5 * 13;
	if (sidebarWidth.value < minWidth) {
		sidebarWidth.value = minWidth;
	}
	if (sidebar.value) {
		sidebar.value.style.width = sidebarWidth.value + "px";
	}
}
</script>

<template>
	<div id="contentSidebar" ref="sidebar" class="px-8 py-6">
		<h5 class="headline-2 flex items-center space-x-2 text-black">
			<slot name="header" />
		</h5>
		<div class="mt-4">
			<slot name="content" />
		</div>
	</div>
</template>
