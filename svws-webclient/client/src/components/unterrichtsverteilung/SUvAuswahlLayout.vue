<template>
	<div class="page page-flex-row pt-6">
		<div class="uv-deaktivierbar h-full w-1/3 min-w-112 max-w-152 shrink-0 overflow-hidden" :inert="bearbeitungAktiv">
			<slot name="auswahl" />
		</div>
		<div class="min-w-0 flex-1 overflow-auto">
			<slot />
		</div>
	</div>
</template>

<script setup lang="ts">
	withDefaults(defineProps<{
		bearbeitungAktiv?: boolean;
	}>(), {
		bearbeitungAktiv: false,
	});
</script>
