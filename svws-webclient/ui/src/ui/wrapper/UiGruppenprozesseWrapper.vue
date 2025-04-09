<template>
	<div class="w-full flex items-center min-h-8">
		<div @click="revert" class="h-full min-h-8 w-6">
			<div v-if="isPending" class="h-full min-h-8 bg-ui-brand w-3 rounded" />
			<!-- Tooltip der anzeigt was in props Daten steht -->
		</div>
		<div ref="container" class="w-10/12">
			<slot />
		</div>
		<div class="h-full w-1/12 ">
			<div v-if="nullable" class="ml-2 w-2 h-full center content-center" @click="deleteContent">
				<span class="inline-block icon i-ri-delete-bin-line" :class="{ 'icon-ui-danger': !data.isEmpty(), 'icon-ui-disabled': data.isEmpty() }" />
			</div>
		</div>
	</div>
</template>

<script setup lang='ts'>

	import { ref } from 'vue';
	import { defineEmits } from 'vue';
	import type { List } from "../../../../core/src/java/util/List";

	const container = ref<HTMLElement | null>(null);

	const props = withDefaults(defineProps<{
		isPending?: boolean, // Gibt an, dass eine Änderung innerhalb der Komponente aktiv ist
		nullable?: boolean, // Gibt an, ob die Werte des Gruppenprozesses gelöscht werden können.
		data: List<string>, // Beschreibung der Daten die vorhanden sind.
	}>(), {
		isPending: false,
		nullable: true,
	});

	const emit = defineEmits(['delete', "revert"]);

	function revert() {
		emit('revert');
	}

	function deleteContent() {
		emit('delete');
	}

</script>

