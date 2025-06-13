
<template>
	<Story title="Sidebar/Menu Header" id="svws-ui-menu-header" :layout="{ type: 'single', iframe: false }" group="app">
		<svws-ui-menu-header @click="onClick">Menu Header</svws-ui-menu-header>
	</Story>
</template>

<script setup lang="ts">
	import { logEvent } from '~/stories/helper';

	function onClick(event: Event) {
		logEvent("click", event);
	}
</script>
