<template>
	<p v-if="focusSwitchingEnabled && focusHelpVisible" class="region-enumeration sub-nav">7</p>
	<div class="svws-ui-sub-nav">
		<div class="svws-ui-sub-nav--wrapper">
			<div v-if="state.scrolled" class="svws-ui-sub-nav--scroll-button bg-gradient-to-l pl-1" @click="scroll('left', true)">
				<svws-ui-button type="icon">
					<span class="icon i-ri-arrow-left-s-line" />
				</svws-ui-button>
			</div>
			<div ref="subNavListElement" class="router-tab-bar--subnav" :class="{'router-tab-bar--subnav--tabs': type === 'tabs', 'focus-region': focusSwitchingEnabled, 'highlighted': focusHelpVisible}">
				<slot />
			</div>
			<div v-if="!state.scrolledMax" class="svws-ui-sub-nav--scroll-button bg-gradient-to-r justify-end -right-1 pr-2" @click="scroll('right', true)">
				<svws-ui-button type="icon">
					<span class="icon i-ri-arrow-right-s-line" />
				</svws-ui-button>
			</div>
		</div>
	</div>
</template>

<script setup lang="ts">
	import { onMounted, onUnmounted, onUpdated, ref } from "vue";

	const props = withDefaults(defineProps<{
		type?: 'default' | 'tabs';
		focusSwitchingEnabled? : boolean;
		focusHelpVisible? : boolean;
	}>(), {
		type: 'default',
		focusSwitchingEnabled: false,
		focusHelpVisible: false,
	});

	onMounted(() => {
		document.body.classList.add('svws-has-sub-nav');
		state.value.maxScrollLeft = (subNavListElement.value?.scrollWidth ?? 0) - (subNavListElement.value?.clientWidth ?? 0);
		state.value.scrolledMax = (subNavListElement.value?.scrollLeft ?? 0) >= state.value.maxScrollLeft;
		subNavListElement.value?.addEventListener("scroll", handleScroll);
		window.addEventListener("resize", handleScroll);
	});

	onUnmounted(() => {
		document.body.classList.remove('svws-has-sub-nav');
		subNavListElement.value?.removeEventListener("scroll", handleScroll);
		window.removeEventListener("resize", handleScroll);
	});

	onUpdated(() => {
		handleScroll();
	});

	type ComponentData = {
		scrolled: boolean;
		scrolledMax: boolean;
		scrollFactor: number;
		maxScrollLeft: number;
		scrollOffset: number;
	}

	const state = ref<ComponentData>({
		scrolled: false,
		scrolledMax: false,
		scrollFactor: 4,
		maxScrollLeft: 0,
		scrollOffset: 12,
	});

	const subNavListElement = ref();

	function handleScroll() {
		state.value.scrolled = (subNavListElement.value?.scrollLeft ?? 0) > state.value.scrollOffset;
		state.value.maxScrollLeft =
			(subNavListElement.value?.scrollWidth ?? 0) - (subNavListElement.value?.clientWidth ?? 0);
		state.value.scrolledMax = (subNavListElement.value?.scrollLeft ?? 0) >= state.value.maxScrollLeft - state.value.scrollOffset;

	}

	function scroll(direction: 'left' | 'right', isSubNav: boolean) {
		const dir = direction === "left" ? -1 : 1;

		subNavListElement.value?.scrollBy({
			top: 0,
			left: (dir * subNavListElement.value.scrollWidth) / state.value.scrollFactor,
			behavior: "smooth",
		});
	}

</script>
