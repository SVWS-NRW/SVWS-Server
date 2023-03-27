<script setup lang="ts">
</script>

<template>
	<Story title="SVWS UI/Popover (Deprecated)">
		<p class="font-bold">Deprected: Bitte nur noch die Komponente SvwsUiTooltip benutzen.</p>
		<div class="h-screen w-screen flex items-center justify-center opacity-50 cursor-not-allowed">
			<svws-ui-popover>
				<template #trigger>
					<span class="border-b border-dashed cursor-help">Hier versteckt sich ein Tooltip</span>
				</template>
				<template #content>
					<svws-ui-tooltip>
						Das ist ein Tooltip
					</svws-ui-tooltip>
				</template>
			</svws-ui-popover>
		</div>
	</Story>
</template>
