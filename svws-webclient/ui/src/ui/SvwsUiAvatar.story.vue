<template>
	<Story title="Avatar" id="svws-ui-avatar" icon="ri:user-line" :layout="{ type: 'single', iframe: false }">
		<Variant title="Default" id="Default">
			<div class="p-4">
				<div class="w-20">
					<svws-ui-avatar @image:captured="logEvent('Image captured', $event)" @image:uploaded="logEvent('Image Uploaded', $event)" />
				</div>
			</div>
		</Variant>
	</Story>
</template>

<script setup lang="ts">
	import { logEvent } from '~/stories/helper';
</script>
