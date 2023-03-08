<script setup lang="ts">
	import { ref } from 'vue';
	import { logEvent } from 'histoire/client';

	const isCollapsed = ref(false);

	function onToggle(event: Event) {
		isCollapsed.value = !isCollapsed.value;
		logEvent("toggle", event);
	}
</script>

<template>
	<Story title="SVWS UI/Layout/Sidebar/Menu">
		<svws-ui-menu :collapsed="isCollapsed" @toggle="onToggle">
			<template #header>
				<svws-ui-menu-header :collapsed="isCollapsed">SVWS-NRW</svws-ui-menu-header>
			</template>
			<template #default>
				<svws-ui-menu-item icon="team" :collapsed="isCollapsed" active>Schüler</svws-ui-menu-item>
				<svws-ui-menu-item icon="building" :collapsed="isCollapsed">Schule</svws-ui-menu-item>
				<svws-ui-menu-item icon="book-open" :collapsed="isCollapsed">Kataloge</svws-ui-menu-item>
				<svws-ui-menu-item icon="donut-chart" :collapsed="isCollapsed">Extras</svws-ui-menu-item>
			</template>
			<template #footer>
				<svws-ui-menu-item icon="settings-2" :collapsed="isCollapsed">Einstellungen</svws-ui-menu-item>
				<svws-ui-menu-item icon="logout-box" subline="Schule XYZ" :collapsed="isCollapsed">Administrator</svws-ui-menu-item>
			</template>
		</svws-ui-menu>
	</Story>
</template>