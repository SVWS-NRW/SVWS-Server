<script setup lang="ts">
	import { logEvent } from 'histoire/client';

	function onClick(event: Event) {
		logEvent("click", event);
	}
</script>

<template>
	<Story title="Sidebar/Menu Header" id="svws-ui-menu-header" group="app">
		<svws-ui-menu-header @click="onClick">Menu Header</svws-ui-menu-header>
	</Story>
</template>
