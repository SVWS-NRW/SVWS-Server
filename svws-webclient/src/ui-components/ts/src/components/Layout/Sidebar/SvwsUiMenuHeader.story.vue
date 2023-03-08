<script setup lang="ts">
	import { logEvent } from 'histoire/client';

	function onClick(event: Event) {
		logEvent("click", event);
	}
</script>

<template>
	<Story title="SVWS UI/Layout/Sidebar/Menu Header">
		<svws-ui-menu-header @click="onClick">Menu Header</svws-ui-menu-header>
	</Story>
</template>