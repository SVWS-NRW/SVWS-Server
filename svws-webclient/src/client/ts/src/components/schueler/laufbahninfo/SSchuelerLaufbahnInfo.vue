<template>
	<div class="page--content">
		<svws-ui-content-card title="Abschlüsse und Berechtigungen an aktueller Schule" class="opacity-50">
			<svws-ui-input-wrapper>
				<div class="opacity-50">
					Hier werden die Informationen zu den Abschlüssen und Berechtigungen an der aktuellen Schule angezeigt
				</div>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Sprachenfolge" class="opacity-50">
			<svws-ui-input-wrapper>
				<div class="opacity-50">
					Hier werden die Informationen zu den Sprachenfolgen angezeigt
				</div>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
		<svws-ui-content-card title="Sprachprüfungen" class="opacity-50">
			<svws-ui-input-wrapper>
				<div class="opacity-50">
					Hier werden die Informationen zu den geplanten bzw. durchgeführten Sprachprüfungen angezeigt
				</div>
			</svws-ui-input-wrapper>
		</svws-ui-content-card>
	</div>
</template>

<script setup lang="ts">
	import type { SchuelerLaufbahninfoProps } from './SchuelerLaufbahninfoProps';

	const props = defineProps<SchuelerLaufbahninfoProps>();

</script>
