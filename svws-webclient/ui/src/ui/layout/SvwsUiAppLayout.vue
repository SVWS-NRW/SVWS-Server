<template>
	<div class="app--layout" :class="{ 'app--layout--has-aside': $slots.aside,
		's-sidebar': $slots.secondaryMenu && hasSecondaryMenu,
		's-all-sidebars-collapsed' : (!$slots.secondaryMenu || !hasSecondaryMenu || !sidebarExpanded) && (!$slots.tertiaryMenu || !hasTertiaryMenu || !secondSidebarExpanded),
		's-sidebar-collapsed': !sidebarExpanded, 's-second-sidebar-collapsed': !secondSidebarExpanded }">
		<div ref="appMenu" v-if="($slots.sidebar && !fullwidthContent)" class="app--menu print:!hidden" :class="{ 'has-scrollbar': hasScrollbar }" :style="{ '--scrollbar-width': scrollbarWidth + 'px' }">
			<slot name="sidebar" />
		</div>
		<div v-if="$slots.secondaryMenu && hasSecondaryMenu" class="app--sidebar print:!hidden" :class="{ 'app--sidebar-normal' : !secondaryMenuSmall }">
			<div class="s-toggle-first">
				<svws-ui-tooltip>
					<button type="button" @click="updateSidebarExpanded">
						<span class="icon i-ri-menu-fold-line" v-if="sidebarExpanded" />
						<span class="icon i-ri-menu-unfold-line" v-else />
					</button>
					<template #content>
						Sidebar {{ !sidebarExpanded ? 'einblenden' : 'ausblenden' }}
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="app--sidebar-container">
				<slot name="secondaryMenu" />
			</div>
		</div>
		<div v-if="$slots.tertiaryMenu && hasTertiaryMenu" class="app--second-sidebar print:!hidden" :class="{ 'app--sidebar-normal' : !tertiaryMenuSmall }">
			<div class="s-toggle-second">
				<svws-ui-tooltip>
					<button type="button" @click="updateSecondSidebarExpanded">
						<span class="icon i-ri-menu-fold-line" v-if="secondSidebarExpanded" />
						<span class="icon i-ri-menu-unfold-line" v-else />
					</button>
					<template #content>
						Second Sidebar {{ !secondSidebarExpanded ? 'einblenden' : 'ausblenden' }}
					</template>
				</svws-ui-tooltip>
			</div>
			<div class="app--second-sidebar-container">
				<slot name="tertiaryMenu" />
			</div>
		</div>
		<div class="app--content h-full">
			<div class="app--content-container relative" :class="{ 'fullwidth-content' : fullwidthContent }">
				<svws-ui-header class="print:!hidden" v-if="false" />
				<slot name="main" />
			</div>
			<aside class="app-layout--aside print:!hidden" v-if="$slots.aside">
				<div class="app-layout--aside-container relative">
					<slot name="aside" />
				</div>
			</aside>
		</div>
	</div>
</template>

<script setup lang='ts'>

	import { onBeforeUnmount, onMounted, ref, computed } from "vue";

	const props = withDefaults(defineProps<{
		fullwidthContent?: boolean;
		noSecondaryMenu?: boolean;
		tertiaryMenu?: boolean;
		secondaryMenuSmall?: boolean;
		tertiaryMenuSmall?: boolean;
	}>(), {
		fullwidthContent: false,
		noSecondaryMenu: false,
		tertiaryMenu: false,
		secondaryMenuSmall: false,
		tertiaryMenuSmall: false,
	});

	const sidebarExpanded = ref<boolean>(true);
	const secondSidebarExpanded = ref<boolean>(true);
	const appMenu = ref<HTMLElement | null>(null);
	const hasScrollbar = ref(false);
	const scrollbarWidth = ref(0);

	const hasSecondaryMenu = computed<boolean>(() => !props.fullwidthContent && !props.noSecondaryMenu);
	const hasTertiaryMenu = computed<boolean>(() => !props.fullwidthContent && props.tertiaryMenu);

	const scrollbarObserver = new ResizeObserver((entries) => {
		entries.forEach((entry) => {
			// Endlosschleife vermeiden
			requestAnimationFrame(() => {
				scrollbarWidth.value = (entry.target as HTMLElement).offsetWidth - entry.target.clientWidth;
				hasScrollbar.value = scrollbarWidth.value > 0 ? true : false;
			});
		});
	});

	onMounted(() => {
		if (appMenu.value !== null)
			scrollbarObserver.observe(appMenu.value);
	});

	onBeforeUnmount(() => {
		if (appMenu.value !== null)
			scrollbarObserver.unobserve(appMenu.value);
	});

	function updateSidebarExpanded(): void {
		sidebarExpanded.value = !sidebarExpanded.value;
		localStorage.setItem('sidebarExpanded', sidebarExpanded.value ? 'true' : 'false');
	}

	if (localStorage.getItem('sidebarExpanded') !== null) {
		sidebarExpanded.value = localStorage.getItem('sidebarExpanded') === 'true';
	}

	function updateSecondSidebarExpanded(): void {
		secondSidebarExpanded.value = !secondSidebarExpanded.value;
		localStorage.setItem('secondSidebarExpanded', secondSidebarExpanded.value ? 'true' : 'false');
	}

	if (localStorage.getItem('secondSidebarExpanded') !== null) {
		secondSidebarExpanded.value = localStorage.getItem('secondSidebarExpanded') === 'true';
	}

</script>
