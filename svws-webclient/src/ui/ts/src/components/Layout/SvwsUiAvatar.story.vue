<script setup lang="ts">
	import { logEvent } from 'histoire/client';
</script>

<template>
	<Story title="Avatar" icon="ri:user-line" :layout="{type: 'grid', width: '90%'}">
		<Variant title="Default">
			<div class="w-20">
				<svws-ui-avatar @image:captured="logEvent('Image captured', $event)" @image:uploaded="logEvent('Image Uploaded', $event)" />
			</div>
		</Variant>
	</Story>
</template>
